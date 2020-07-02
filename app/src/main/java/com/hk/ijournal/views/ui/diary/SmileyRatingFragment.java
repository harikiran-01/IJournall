package com.hk.ijournal.views.ui.diary;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapePath;
import com.hk.ijournal.R;
import com.hk.ijournal.databinding.FragmentSmileyRatingBinding;


public class SmileyRatingFragment extends DialogFragment {
    private View source;
    private FragmentSmileyRatingBinding fragmentSmileyRatingBinding;

    public SmileyRatingFragment(View source) {
        this.source = source;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Smiley Rating");
        //set the dialog to non-modal and disable dim out fragment behind
        Window window = dialog.getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycle", "smileyF onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentSmileyRatingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_smiley_rating, container, false);
        return fragmentSmileyRatingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogPosition();
    }

    private void setDialogPosition() {
        if (source == null) {
            return;
        }
        ShapePath shapePath = new ShapePath();
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel();
        shapeAppearanceModel.getBottomEdge().getEdgePath(4.0f, 4.0f, 2.0f, shapePath);
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        materialShapeDrawable.setPaintStyle(Paint.Style.FILL);

        // Find out location of source component on screen
        int[] location = new int[2];
        source.getLocationOnScreen(location);
        int sourceX = location[0];
        int sourceY = location[1];

        Window window = getDialog().getWindow();

        // set "origin" to top left corner
        window.setGravity(Gravity.TOP | Gravity.START);

        WindowManager.LayoutParams params = window.getAttributes();

        //editing params based on source position
        params.x = sourceX - dpToPx(110); // about half of confirm button size left of source view
        params.y = sourceY - dpToPx(80); // above source view

        window.setAttributes(params);
    }

    public int dpToPx(float valueInDp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

}