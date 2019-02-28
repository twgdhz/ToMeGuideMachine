//package com.guidemachine.ui.guide.view;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Handler;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.PopupWindow;
//import android.widget.Spinner;
//
//import com.guidemachine.R;
//import com.guidemachine.service.entity.BaseBean;
//import com.guidemachine.service.entity.FencInfoBean;
//import com.guidemachine.service.entity.GoodSpecBean;
//
//public class AddFencePop extends PopupWindow {
//    private Context context;
//    FencInfoBean fencInfoBean;
//    private String fenceType;
//    private String scenerySpot;
//    CreateFenceCallBack createFenceCallBack;
//
//    public AddFencePop(Activity context) {
//        super(context);
//        this.context = context;
//        init(context);
//    }
//
//    public void init(final Activity context) {
//        View view = View.inflate(context, R.layout.add_fence_pop, null);
//        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        this.setBackgroundDrawable(new BitmapDrawable());
//        this.setOutsideTouchable(true);
//        this.setContentView(view);
//
//        final Spinner spFenceType = view.findViewById(R.id.sp_fence_type);//围栏类型
//        Spinner spFenceSpot = view.findViewById(R.id.sp_scenery_spot);//所属景点
//        Button btnCancel = view.findViewById(R.id.btn_cancel);
//        Button btnEnsure = view.findViewById(R.id.btn_ensure);
//        final EditText etName = view.findViewById(R.id.et_name);//围栏名称
////        etName.setFocusable(true);
////        etName.setFocusableInTouchMode(true);
////        etName.requestFocus();
//            Handler handle=new Handler();
//            handle.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    InputMethodManager inputMethodManager=(InputMethodManager) etName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputMethodManager.showSoftInput(etName, 0);
//                }
//            }, 0);
//        final EditText etInFence = view.findViewById(R.id.et_in_fence);
//        final EditText etOutFence = view.findViewById(R.id.et_out_fence);
//        final String[] arrFenceType = {"入围栏", "出围栏", "出入围栏"};
//        final String[] arrFenceSpot = {"薛涛井", "望江楼", "天涯海角"};
//        //创建ArrayAdapter对象
//        //此处有知识点：https://www.jianshu.com/p/ccb5930f4a16    android:spinnerMode="dialog"
//        ArrayAdapter<String> arrFenceTypeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, arrFenceType);
//        arrFenceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spFenceType.setAdapter(arrFenceTypeAdapter);
//        spFenceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                fenceType = arrFenceType[i];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        final ArrayAdapter<String> arrFenceSpotAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, arrFenceSpot);
//        arrFenceSpotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spFenceSpot.setAdapter(arrFenceSpotAdapter);
//        spFenceSpot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                scenerySpot = arrFenceSpot[i];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
//        btnEnsure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fencInfoBean = new FencInfoBean();
//                fencInfoBean.setFenceName(etName.getText().toString());
//                fencInfoBean.setFenceTpe(fenceType);
//                fencInfoBean.setScenerySpot(scenerySpot);
//                fencInfoBean.setInFenceNotice(etInFence.getText().toString());
//                fencInfoBean.setOutFenceNotice(etOutFence.getText().toString());
//                if (createFenceCallBack!=null){
//                    createFenceCallBack.createFence(fencInfoBean);
//                }
//            }
//        });
//    }
//
//    /**
//     * 显示popwindow
//     */
//    public void showPopupWindow(View view) {
//        if (!isShowing()) {
////            MyPopUpWindow popupWindow=new MyPopUpWindow();
////            popupWindow.showAsDropDown(view);
////            this.showAsDropDown(view);
//            this.showAtLocation(view, Gravity.CENTER, 0, 0);
//        }
//    }
//
//    public interface CreateFenceCallBack {
//        void createFence(FencInfoBean fencInfoBean);
//    }
//}
