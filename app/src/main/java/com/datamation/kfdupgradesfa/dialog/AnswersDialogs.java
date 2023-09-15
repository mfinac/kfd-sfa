package com.datamation.kfdupgradesfa.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.datamation.kfdupgradesfa.R;

import androidx.core.content.ContextCompat;

/*
    create by kaveesha - 01/12/2020 ......
 */

public class AnswersDialogs {
    Context context;

    public AnswersDialogs(Context context) {
        this.context = context;
    }

    //------------------------Place an orders ---------------------------------------------------------

    public void startOrder() {
        final Dialog startOrder = new Dialog(context);
       startOrder.requestWindowFeature(Window.FEATURE_NO_TITLE);
       startOrder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       startOrder.setCancelable(false);
       startOrder.setCanceledOnTouchOutside(false);
       startOrder.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) startOrder.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) startOrder.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) startOrder.findViewById(R.id.image2);
        answer.setText("1). You can place an order by clicking on the place order icon in the navigation bar.Then you can go to the order header layout. \n\n" +
                "2). Then you must choose a delivery date and pay type.\n\n" +
                "• Before going to the detail tab, the user should tap the arrow icon at the bottom of the invoice header and save the header details.(It is essential) \n\n" +
                "3). Then You can view item details in detail tab.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dashboard));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.order_header));

        //close
        startOrder.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOrder.dismiss();
            }
        });
        startOrder.show();
    }


    public void AddItems() {
        final Dialog AddItemsDialog = new Dialog(context);
        AddItemsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddItemsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AddItemsDialog.setCancelable(false);
        AddItemsDialog.setCanceledOnTouchOutside(false);
        AddItemsDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) AddItemsDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) AddItemsDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) AddItemsDialog.findViewById(R.id.image2);

        answer.setText("1). You can tap on Search bar to search the required item.\n\n" +
                "2). Then you should tap plus button to add items.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.order_detail));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.order_detail_search));

        //close
        AddItemsDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemsDialog.dismiss();
            }
        });
        AddItemsDialog.show();
    }

    public void calculateFreeIssue() {
        final Dialog FreeDialog = new Dialog(context);
        FreeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FreeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FreeDialog.setCancelable(false);
        FreeDialog.setCanceledOnTouchOutside(false);
        FreeDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) FreeDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) FreeDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) FreeDialog.findViewById(R.id.image2);

        answer.setText("1). Before going to the summary tab, the you should tap the arrow icon at the top of the order detail. Then you can get free issue items.\n\n" +
                "2). After tapping arrow icon you can go to the summary tab and you can view summary of the order details and free item details.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.free_calculate_icon));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.free));

        //close
        FreeDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FreeDialog.dismiss();
            }
        });
        FreeDialog.show();
    }

    public void saveOrder() {
        final Dialog saveDialog = new Dialog(context);
        saveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        saveDialog.setCancelable(false);
        saveDialog.setCanceledOnTouchOutside(false);
        saveDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) saveDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) saveDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) saveDialog.findViewById(R.id.image2);

        answer.setText("1). If all information are correct you should tap fab Button in bottom of the order summary to view save icon.\n\n" +
                "2). After tapping save button you can view save dialog box.\n\n" +
                "3). Then you can save order by tapping yes button in the dialog.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.save));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.save_invoice));

        //close
        saveDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDialog.dismiss();
            }
        });
        saveDialog.show();
    }

    public void editPlaceAnOrder() {
        final Dialog editorderDialog = new Dialog(context);
        editorderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editorderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editorderDialog.setCancelable(false);
        editorderDialog.setCanceledOnTouchOutside(false);
        editorderDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) editorderDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) editorderDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) editorderDialog.findViewById(R.id.image2);

        answer.setText("1). To do this you need to click on the edit icon in the order summary.\n\n" +
                "2). After tapping the edit icon you will be able to go back to the order detail.\n\n" +
                "3). Then you can edit order.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.order_summary_edit));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.order_edit));

        //close
        editorderDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorderDialog.dismiss();
            }
        });
        editorderDialog.show();
    }

    public void deletePlaceAnOrder() {
        final Dialog deleteorderDialog = new Dialog(context);
        deleteorderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteorderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteorderDialog.setCancelable(false);
        deleteorderDialog.setCanceledOnTouchOutside(false);
        deleteorderDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) deleteorderDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) deleteorderDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) deleteorderDialog.findViewById(R.id.image2);

        answer.setText("1). To undo order you should tap discard icon.\n\n" +
                "2). After that discard dialog box will appear.\n\n" +
                "3). Then you can undo order by tapping yes button in the dialog");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.order_discard_icon));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.discard_order_dialog));

        //close
        deleteorderDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteorderDialog.dismiss();
            }
        });
        deleteorderDialog.show();
    }
//---------------------------- View  Orders -------------------------------------------------------------------------
    public void editOrders() {
        final Dialog editDialog = new Dialog(context);
        editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editDialog.setCancelable(false);
        editDialog.setCanceledOnTouchOutside(false);
        editDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) editDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) editDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) editDialog.findViewById(R.id.image2);

        answer.setText("1). If you want to edit previous order , you should tap edit button in order tab in the dashboard.\n\n" +
                "2). Then you can go to the order header.\n\n" +
                "3). There, you can see previous order items in the order detail.\n\n" +
                "4). After the changing order you should tap save button to save the new changes.\n\n" +
                "5). Also, you can undo new changes by tapping discard button.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.previous_order_edit));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.previous_order_edit_items));


        //close
        editDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }

    public void deleteOrders() {
        final Dialog deleteDialog = new Dialog(context);
        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.setCancelable(false);
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) deleteDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) deleteDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) deleteDialog.findViewById(R.id.image2);

        answer.setText("1). You should tap minus button to delete the order.\n\n" +
                "• But you can delete the order only if the order is not uploaded...");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.previous_order_delete));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.delete_order));

        //close
        deleteDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    public void uploadOrders() {
        final Dialog uploadDialog = new Dialog(context);
       uploadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       uploadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       uploadDialog.setCancelable(false);
       uploadDialog.setCanceledOnTouchOutside(false);
       uploadDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) uploadDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) uploadDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) uploadDialog.findViewById(R.id.image2);

        answer.setText("1). To upload order you should tap 'click to confirm' button.\n\n" +
                "2). You can view 'click to confirm' button color is green and 'Not Sync' button " +
                "color is white before the upload order.\n\n" +
                "3). You can view 'click to confirm' button is change to 'click to refresh' and " +
                "'Not sync' button is change to 'CONFIRMED' after the upload .\n\n" +
                "4). After the upload order you can view 'Not sync' button colour is change to brown.\n\n" +
                "5). Also, after the upload order you can view 'click to confirm' button color is change to white.\n\n" +
                "6). You will then see that your order has been successfully uploaded.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.before_upload));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.after_upload));


        //close
        uploadDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDialog.dismiss();
            }
        });
        uploadDialog.show();
    }

    public void checkOrderApprovalOrReject() {
        final Dialog checkDialog = new Dialog(context);
        checkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkDialog.setCancelable(false);
        checkDialog.setCanceledOnTouchOutside(false);
        checkDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) checkDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) checkDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) checkDialog.findViewById(R.id.image2);

        answer.setText("1). You can see if your order has been approved or rejected by tapping 'click to refresh' button.\n\n" +
                "2). If your order is rejected , 'CONFIRMED' button is change to 'REJECTED'.\n\n" +
                "3).  Button colour is change to red colour.\n\n" +
                "4). If your order is approval , 'CONFIRMED' button is change to 'APPROVED'.\n\n" +
                "5). Button colour is change to green colour.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.before_upload));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.after_upload));

        //close
        checkDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDialog.dismiss();
            }
        });
        checkDialog.show();
    }

    public void checkOrderDispatchOrInvoice() {
        final Dialog checkDialog = new Dialog(context);
        checkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkDialog.setCancelable(false);
        checkDialog.setCanceledOnTouchOutside(false);
        checkDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) checkDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) checkDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) checkDialog.findViewById(R.id.image2);

        answer.setText("1). You can see if your order has been dispatch or invoice by tapping 'click to refresh' button.\n\n" +
                "2). If your order is dispatch , 'APPROVED' button is change to 'DISPATCH'.\n\n" +
                "3). Button colour is change to purple colour.");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.before_upload));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.after_upload));

        //close
        checkDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDialog.dismiss();
            }
        });
        checkDialog.show();
    }

    //-------------------------- view outstanding details --------------------------------------

    public void viewOutstanding() {
        final Dialog viewDialog = new Dialog(context);
        viewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewDialog.setCancelable(false);
        viewDialog.setCanceledOnTouchOutside(false);
        viewDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) viewDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) viewDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) viewDialog.findViewById(R.id.image2);

      //  answer.setText("");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.outstanding_tab));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.outstanding));


        //close
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDialog.dismiss();
            }
        });
        viewDialog.show();
    }

    //------------------- To view Payment details -------------------------------------------------------------

    public void viewPayments() {
        final Dialog viewDialog = new Dialog(context);
        viewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewDialog.setCancelable(false);
        viewDialog.setCanceledOnTouchOutside(false);
        viewDialog.setContentView(R.layout.dialog_answers);

        final TextView answer = (TextView) viewDialog.findViewById(R.id.ans);
        final ImageView image1 = (ImageView) viewDialog.findViewById(R.id.image1);
        final ImageView image2 = (ImageView) viewDialog.findViewById(R.id.image2);

        //  answer.setText("");

        image1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.payment_tab));
        image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.payments));

        //close
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDialog.dismiss();
            }
        });
        viewDialog.show();
    }
}
