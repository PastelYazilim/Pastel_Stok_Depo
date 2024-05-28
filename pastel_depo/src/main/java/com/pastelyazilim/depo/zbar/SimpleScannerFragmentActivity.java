package com.pastelyazilim.depo.zbar;

import android.os.Bundle;

import com.pastelyazilim.depo.R;

/**
 *
 */
public class SimpleScannerFragmentActivity extends BaseScannerActivity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner_fragment);
        setupToolbar();
    }
}