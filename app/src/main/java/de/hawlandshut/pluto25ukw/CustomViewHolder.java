package de.hawlandshut.pluto25ukw;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder  extends RecyclerView.ViewHolder {
    public final TextView mLine1;
    public final TextView mLine2;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        mLine1 = (TextView) itemView.findViewById( R.id.post_view_line1);
        mLine2 = (TextView) itemView.findViewById( R.id.post_view_line2);
    }
}
