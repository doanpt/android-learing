package com.cnc.hcm.cnctrack.event;

import android.view.MenuItem;

public interface RecyclerViewMenuItemClickListener {
    void onRecyclerViewMenuItemClicked(int position, MenuItem menuItem);
    void onRecyclerViewMenuItemClickedFailue(String message);
}
