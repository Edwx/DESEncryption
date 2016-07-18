package pe.edu.utp.desencryption.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pe.edu.utp.desencryption.R;

public class DrawerAdapter extends BaseAdapter {

	private List<Drawer> mDrawerItems;
	private LayoutInflater mInflater;

	public DrawerAdapter(Context context, List<Drawer> list) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDrawerItems = list;
	}

	@Override
	public int getCount() {
		return mDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mDrawerItems.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_view_item_drawer, parent, false);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon_social_navigation_item);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Drawer item = mDrawerItems.get(position);

		holder.icon.setImageResource(item.getIconRes());
		holder.title.setText(item.getText());

		return convertView;
	}

	private static class ViewHolder {
		public ImageView icon;
		public TextView title;
	}
}
