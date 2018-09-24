package com.solo.movinfo.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.solo.movinfo.R;
import com.solo.movinfo.utils.NetworkUtils;

import timber.log.Timber;

public abstract class InternetAwareFragment extends Fragment {

    private Snackbar mInternetConnectionSnackbar;

    private ConnectivityStateChangeReceiver mConnectivityStateChangeReceiver;

    // Actions to be performed when internet connection is OFF
    protected abstract void onInternetConnectivityOff();

    // Actions to be performed when internet connection is ON
    protected abstract void onInternetConnectivityOn();

    @Override
    public void onStart() {
        super.onStart();
        registerConnectivityChangeReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterConnectivityChangeReceiver();
    }

    private void registerConnectivityChangeReceiver() {
        mConnectivityStateChangeReceiver = new ConnectivityStateChangeReceiver();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(mConnectivityStateChangeReceiver, intentFilter);
    }

    private void unregisterConnectivityChangeReceiver() {
        requireActivity().unregisterReceiver(mConnectivityStateChangeReceiver);
    }


    /**
     * Monitors changes in the device internet connectivity
     */
    private class ConnectivityStateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = NetworkUtils.isInternetConnected(requireContext());

            if (isInitialStickyBroadcast() && isConnected) {
                onInternetConnectivityOn();
            }

            if (!isInitialStickyBroadcast() && isConnected) {
                Timber.d("Connected!");
                showInternetConnectivityIsOn();
            }

            if (!isConnected) {
                Timber.d("Not Connected!");
                showInternetConnectivityIsOff();
            }

        }

        /**
         * Display Snackbar if internet is OFF
         * and perform any actions specified in the onInternetConnectivityOff()
         */
        void showInternetConnectivityIsOff() {
            if (mInternetConnectionSnackbar != null && mInternetConnectionSnackbar.isShown()) {
                mInternetConnectionSnackbar.dismiss();
            }

            mInternetConnectionSnackbar = Snackbar.make(
                    requireActivity().findViewById(R.id.single_fragment),
                    getString(R.string.no_internet_connection_message),
                    Snackbar.LENGTH_INDEFINITE);
            mInternetConnectionSnackbar.show();

            onInternetConnectivityOff();
        }

        /**
         * Display Snackbar if internet is ON
         * and perform any actions specified in the onInternetConnectivityOn()
         */
        void showInternetConnectivityIsOn() {
            if (mInternetConnectionSnackbar != null && mInternetConnectionSnackbar.isShown()) {
                mInternetConnectionSnackbar.dismiss();
            }

            mInternetConnectionSnackbar = Snackbar.make(
                    requireActivity().findViewById(R.id.single_fragment),
                    getString(R.string.connected_to_internet),
                    Snackbar.LENGTH_SHORT);

            mInternetConnectionSnackbar.getView()
                    .setBackgroundColor(getResources().getColor(R.color.connectedColor));

            mInternetConnectionSnackbar.show();

            onInternetConnectivityOn();
        }
    }

}
