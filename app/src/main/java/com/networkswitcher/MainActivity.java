package com.networkswitcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openNetworkSettings();
    }

    private void openNetworkSettings() {
        SubscriptionManager sm = (SubscriptionManager)
                getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE);

        List<SubscriptionInfo> sims = null;
        try {
            sims = sm.getActiveSubscriptionInfoList();
        } catch (Exception e) {
            Log.e("NS", "Could not read SIMs", e);
        }

        // Single SIM or no info – go straight to network settings
        if (sims == null || sims.size() <= 1) {
            launchNetworkSettings(sims != null && !sims.isEmpty()
                    ? sims.get(0).getSubscriptionId() : -1);
            return;
        }

        // Dual SIM – show picker dialog
        String[] labels = new String[sims.size()];
        for (int i = 0; i < sims.size(); i++) {
            SubscriptionInfo info = sims.get(i);
            CharSequence name = info.getDisplayName();
            String number = info.getNumber();
            labels[i] = (name != null ? name.toString() : "SIM " + (i + 1))
                    + (number != null && !number.isEmpty() ? "  \u2022  " + number : "");
        }

        final List<SubscriptionInfo> finalSims = sims;
        new AlertDialog.Builder(this)
                .setTitle("Select SIM for network type")
                .setItems(labels, (dialog, which) ->
                        launchNetworkSettings(finalSims.get(which).getSubscriptionId()))
                .setOnCancelListener(d -> finish())
                .show();
    }

    private void launchNetworkSettings(int subId) {
        // Try 1: Direct network operator settings with sub ID (Android 11+)
        if (subId != -1 && tryIntent(new Intent("android.settings.NETWORK_OPERATOR_SETTINGS")
                .putExtra("android.provider.extra.SUB_ID", subId))) return;

        // Try 2: Motorola-specific MobileNetworkActivity
        try {
            Intent i = new Intent();
            i.setClassName("com.android.phone", "com.android.phone.MobileNetworkActivity");
            if (subId != -1) i.putExtra("android.provider.extra.SUB_ID", subId);
            if (tryIntent(i)) return;
        } catch (Exception ignored) {}

        // Try 3: Generic ACTION_NETWORK_OPERATOR_SETTINGS
        if (tryIntent(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
                .putExtra("android.provider.extra.SUB_ID", subId))) return;

        // Try 4: Mobile network settings panel (Android 10+)
        if (tryIntent(new Intent("android.settings.panel.action.MOBILE_DATA"))) return;

        // Fallback: Wireless settings
        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        finish();
    }

    private boolean tryIntent(Intent intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
