package com.doaaramiz.popularmovies.view.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

/**
 * @author doaaramiz
 * @date 24.11.2015
 */
public abstract class BaseFragment extends Fragment {

	protected ProgressDialog progressDialog;

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	protected void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
		}
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Please wait...");
		progressDialog.show();
	}

	protected void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
