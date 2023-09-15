package com.datamation.kfdupgradesfa.model;

import android.content.Context;
import android.util.Log;


import com.datamation.kfdupgradesfa.controller.FreeDebController;
import com.datamation.kfdupgradesfa.controller.FreeDetController;
import com.datamation.kfdupgradesfa.controller.FreeHedController;
import com.datamation.kfdupgradesfa.controller.FreeMslabController;

import com.datamation.kfdupgradesfa.controller.OrderController;

import java.util.ArrayList;
import java.util.List;

public class FreeIssue {
	Context context;

	public FreeIssue(Context context) {
		this.context = context;
	}

	// *-*-*-*-*-*-*-*-*-* Sort out Assort items using an algorithm *-*-**-*-*-*-*-*-*-*-*-*
	public ArrayList<OrderDetail> sortAssortItems(ArrayList<OrderDetail> OrderList) {
		FreeHedController freeHedDS = new FreeHedController(context);
		FreeDetController freeDetDS = new FreeDetController(context);
		ArrayList<OrderDetail> newOrderList = new ArrayList<OrderDetail>();
		List<String> AssortList = new ArrayList<String>();

		for (int x = 0; x <= OrderList.size() - 1; x++) {
			// get Assort list for each Item code
			AssortList = freeDetDS.getAssortByRefno(freeHedDS.getRefoByItemCode(OrderList.get(x).getFORDDET_ITEM_CODE()));

			if (AssortList.size() > 0) {

				for (int y = x + 1; y <= OrderList.size() - 1; y++) {

					for (int i = 0; i <= AssortList.size() - 1; i++) {

						Log.v(" @@@@@ ", OrderList.get(y).getFORDDET_ITEM_CODE() + " - " + AssortList.get(i));

						if (OrderList.get(y).getFORDDET_ITEM_CODE().equals(AssortList.get(i))) {

							OrderList.get(x).setFORDDET_QTY(String.valueOf(Integer.parseInt(OrderList.get(x).getFORDDET_QTY()) + Integer.parseInt(OrderList.get(y).getFORDDET_QTY())));
							OrderList.get(y).setFORDDET_QTY("0");
							Log.v("#####", OrderList.get(y).getFORDDET_QTY() + " + " + OrderList.get(x).getFORDDET_QTY());
						}

					}
				}
			}
		}

		// Remove zero quantities from Arraylist
		for (int i = 0; i <= OrderList.size() - 1; i++) {

			if (!OrderList.get(i).getFORDDET_QTY().equals("0")) {

				newOrderList.add(OrderList.get(i));
			}

			Log.v("$$$$$$", OrderList.get(i).getFORDDET_ITEM_CODE() + " - " + OrderList.get(i).getFORDDET_QTY());
		}

		for (OrderDetail order : newOrderList) {

			Log.v("Himas", order.getFORDDET_ITEM_CODE() + " - " + order.getFORDDET_QTY());

		}
		return newOrderList;

	}

	// *-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-**-*-*-*-*-*-*-*-*


	// *-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-**-*-*-*-*-*-*-*-*

	public ArrayList<FreeItemDetails> getFreeItemsBySalesItem(ArrayList<OrderDetail> newOrderList) {

		ArrayList<FreeItemDetails> freeList = new ArrayList<FreeItemDetails>();
		int slabassorted = 0, flatAssort = 0, mixAssort = 0;
		FreeHedController freeHedDS = new FreeHedController(context);

		ArrayList<OrderDetail> dets = sortAssortItems(newOrderList);

		for (OrderDetail det : dets) {// ---------------------- order list----------------------
			String debCodeStr = new OrderController(context).getRefnoByDebcode(det.getFORDDET_REFNO());
			// crpb00001
			Log.v("forddet", det.getFORDDET_ITEM_CODE());
			// get record from freeHed about valid scheme

			ArrayList<FreeHed> arrayListchk = freeHedDS.getFreeIssueItemDetailByRefnoPre(det.getFORDDET_ITEM_CODE(),debCodeStr);

			ArrayList<FreeHed> arrayList = freeHedDS.getFreeIssueItemDetailByRefno(det.getFORDDET_ITEM_CODE());

			if(arrayListchk.size()>1){
	            	android.widget.Toast.makeText(context, "Not Eligible  FREE ISSUES SCHEMA....!", android.widget.Toast.LENGTH_LONG).show();

			}else{



			// selected item qty
			int entedTotQty = Integer.parseInt(det.getFORDDET_QTY());

			if (entedTotQty > 0) {

				for (FreeHed freeHed : arrayList) {// --------Related free issue
													// list------

					if (freeHed.getFFREEHED_PRIORITY().equals("1")) {
						// flat
						if (freeHed.getFFREEHED_FTYPE().equals("Flat")) {// -------Flat
																			// only-----
							// flat scheme item qty => 12:1
							int itemQty = (int) Float.parseFloat(freeHed.getFFREEHED_ITEM_QTY());
							// Debtor code from order header ref no ORASA/00006
							String debCode = new OrderController(context).getRefnoByDebcode(det.getFORDDET_REFNO());
							// get debtor count from FIS/098 no
							int debCount = new FreeDebController(context).getRefnoByDebCount(freeHed.getFFREEHED_REFNO());
							// select debtor from FIS no & R0001929
							int IsValidDeb = new FreeDebController(context).isValidDebForFreeIssue(freeHed.getFFREEHED_REFNO(), debCode);
							// get assort count from FIS no
							int assortCount = new FreeDetController(context).getAssoCountByRefno(freeHed.getFFREEHED_REFNO());

							// if its assorted
							if (assortCount > 1) {

								flatAssort = flatAssort + entedTotQty;

								int index = 0;
								boolean assortUpdate = false;

								for (FreeItemDetails freeItemDetails : freeList) {

									if (freeItemDetails.getRefno().equals(freeHed.getFFREEHED_REFNO())) {

										freeList.get(index).setRefno(freeHed.getFFREEHED_REFNO());
										freeList.get(index).setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
										freeList.get(index).setFreeQty((int) Math.round(flatAssort / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()));
										assortUpdate = true;
									}

									index++;
								}

								if (!assortUpdate) {// When 1st time running

										// available for everyone

										// IF entered qty/scheme qty is more
										// than 0
										if ((int) Math.round(entedTotQty / itemQty) > 0) {

											FreeItemDetails details = new FreeItemDetails();
											// details.setFreeItem(new
											// FreeItemDS(context).getFreeItemsByRefno(freeHed.getFFREEHED_REFNO()));
											details.setRefno(freeHed.getFFREEHED_REFNO());
											details.setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
											details.setFreeQty((int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()));
											freeList.add(details);

											Log.v("Free Issues ", (int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()) + "");
											entedTotQty = (int) Math.round(entedTotQty % itemQty);
										}

									}

								// if not assorted
							} else {

									if ((int) Math.round(entedTotQty / itemQty) > 0) {

										FreeItemDetails details = new FreeItemDetails();
										// details.setFreeItem(new
										// FreeItemDS(context).getFreeItemsByRefno(freeHed.getFFREEHED_REFNO()));
										details.setRefno(freeHed.getFFREEHED_REFNO());
										details.setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
										details.setFreeQty((int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()));
										freeList.add(details);

										Log.v("Free Issues ", (int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()) + "");
										entedTotQty = (int) Math.round(entedTotQty % itemQty);
									}
							}

						}  else if (freeHed.getFFREEHED_FTYPE().equals("Mix")) {
							// slabAssort

							FreeMslabController freeMslabDS = new FreeMslabController(context);
							final ArrayList<FreeMslab> mixList;
							int assortCount = new FreeDetController(context).getAssoCountByRefno(freeHed.getFFREEHED_REFNO());
							if (assortCount > 1) {// if assorted
								mixAssort = mixAssort + entedTotQty;
								mixList = freeMslabDS.getMixDetails(freeHed.getFFREEHED_REFNO(), mixAssort);
							} else {
								mixList = freeMslabDS.getMixDetails(freeHed.getFFREEHED_REFNO(), entedTotQty);
							}

							for (FreeMslab freeMslab : mixList) {

								// all debtor for freeissues

									if (assortCount > 1) {
										int index = 0;
										boolean assortUpdate = false;
										for (FreeItemDetails freeItemDetails : freeList) {
											if (freeItemDetails.getRefno().equals(freeHed.getFFREEHED_REFNO())) {

												int itemQty = (int) Float.parseFloat(freeMslab.getFFREEMSLAB_ITEM_QTY());
												freeList.get(index).setRefno(freeHed.getFFREEHED_REFNO());
												freeList.get(index).setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
												// Old 19-08-2016 freeList.get(index).setFreeQty((int) Math.round(mixAssort / itemQty) * (int) Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()));
												freeList.get(index).setFreeQty((int) (((Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()))/itemQty) * mixAssort));
												assortUpdate = true;
											}

											index++;
										}

										if (!assortUpdate) {

											FreeItemDetails details = new FreeItemDetails();
											int itemQty = (int) Float.parseFloat(freeMslab.getFFREEMSLAB_ITEM_QTY());
											details.setRefno(freeHed.getFFREEHED_REFNO());
											details.setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
											// Old 19-08-2016 details.setFreeQty((int) Math.round(mixAssort / itemQty) * (int) Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()));
											//Old 22-08-2016  details.setFreeQty((int) Math.round(((Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()))/itemQty) * mixAssort));
											details.setFreeQty((int) (((Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()))/itemQty) * mixAssort));
											freeList.add(details);
										}

									} else {
										FreeItemDetails details = new FreeItemDetails();
										int itemQty = (int) Float.parseFloat(freeMslab.getFFREEMSLAB_ITEM_QTY());
										details.setRefno(freeHed.getFFREEHED_REFNO());
										details.setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
										// Old 19-08-2016 details.setFreeQty((int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()));
										details.setFreeQty((int) (((Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()))/itemQty) * entedTotQty));
										freeList.add(details);
									}
							}
						}
					}else {
						// flat
						if (freeHed.getFFREEHED_FTYPE().equals("Flat")) {// -------Flat
																			// only-----
							// flat scheme item qty => 12:1
							int itemQty = (int) Float.parseFloat(freeHed.getFFREEHED_ITEM_QTY());
							// Debtor code from order header ref no ORASA/00006
							String debCode = new OrderController(context).getRefnoByDebcode(det.getFORDDET_REFNO());
							// get debtor count from FIS/098 no
							int debCount = new FreeDebController(context).getRefnoByDebCount(freeHed.getFFREEHED_REFNO());
							// select debtor from FIS no & R0001929
							int IsValidDeb = new FreeDebController(context).isValidDebForFreeIssue(freeHed.getFFREEHED_REFNO(), debCode);
							// get assort count from FIS no
							int assortCount = new FreeDetController(context).getAssoCountByRefno(freeHed.getFFREEHED_REFNO());

							// if its assorted
							if (assortCount > 1) {

								flatAssort = flatAssort + entedTotQty;

								int index = 0;
								boolean assortUpdate = false;

								for (FreeItemDetails freeItemDetails : freeList) {

									if (freeItemDetails.getRefno().equals(freeHed.getFFREEHED_REFNO())) {

										freeList.get(index).setRefno(freeHed.getFFREEHED_REFNO());
										freeList.get(index).setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
										freeList.get(index).setFreeQty((int) Math.round(flatAssort / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()));
										assortUpdate = true;
									}

									index++;
								}

								if (!assortUpdate) {// When 1st time running

										// available for everyone

										// IF entered qty/scheme qty is more
										// than 0
										if ((int) Math.round(entedTotQty / itemQty) > 0) {

											FreeItemDetails details = new FreeItemDetails();
											// details.setFreeItem(new
											// FreeItemDS(context).getFreeItemsByRefno(freeHed.getFFREEHED_REFNO()));
											details.setRefno(freeHed.getFFREEHED_REFNO());
											details.setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
											details.setFreeQty((int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()));
											freeList.add(details);

											Log.v("Free Issues ", (int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()) + "");
											entedTotQty = (int) Math.round(entedTotQty % itemQty);
										}
								}

								// if not assorted
							} else {
									if ((int) Math.round(entedTotQty / itemQty) > 0) {

										FreeItemDetails details = new FreeItemDetails();
										// details.setFreeItem(new
										// FreeItemDS(context).getFreeItemsByRefno(freeHed.getFFREEHED_REFNO()));
										details.setRefno(freeHed.getFFREEHED_REFNO());
										details.setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
										details.setFreeQty((int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()));
										freeList.add(details);

										Log.v("Free Issues ", (int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeHed.getFFREEHED_FREE_IT_QTY()) + "");
										entedTotQty = (int) Math.round(entedTotQty % itemQty);
									}
							}

						}  else if (freeHed.getFFREEHED_FTYPE().equals("Mix")) {
							// slabAssort

							FreeMslabController freeMslabDS = new FreeMslabController(context);
							final ArrayList<FreeMslab> mixList;
							int assortCount = new FreeDetController(context).getAssoCountByRefno(freeHed.getFFREEHED_REFNO());
							if (assortCount > 1) {// if assorted
								mixAssort = mixAssort + entedTotQty;
								mixList = freeMslabDS.getMixDetails(freeHed.getFFREEHED_REFNO(), mixAssort);
							} else {
								mixList = freeMslabDS.getMixDetails(freeHed.getFFREEHED_REFNO(), entedTotQty);
							}

							for (FreeMslab freeMslab : mixList) {

							// all debtor for freeissues

									if (assortCount > 1) {
										int index = 0;
										boolean assortUpdate = false;
										for (FreeItemDetails freeItemDetails : freeList) {
											if (freeItemDetails.getRefno().equals(freeHed.getFFREEHED_REFNO())) {

												int itemQty = (int) Float.parseFloat(freeMslab.getFFREEMSLAB_ITEM_QTY());
												freeList.get(index).setRefno(freeHed.getFFREEHED_REFNO());
												freeList.get(index).setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
												// Old 19-08-2016 freeList.get(index).setFreeQty((int) Math.round(mixAssort / itemQty) * (int) Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()));
												freeList.get(index).setFreeQty((int) (((Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()))/itemQty) * mixAssort));
												assortUpdate = true;
											}

											index++;
										}

										if (!assortUpdate) {

											FreeItemDetails details = new FreeItemDetails();
											int itemQty = (int) Float.parseFloat(freeMslab.getFFREEMSLAB_ITEM_QTY());
											details.setRefno(freeHed.getFFREEHED_REFNO());
											details.setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
											// Old 19-08-2016 details.setFreeQty((int) Math.round(mixAssort / itemQty) * (int) Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()));
											//Old 22-08-2016  details.setFreeQty((int) Math.round(((Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()))/itemQty) * mixAssort));
											details.setFreeQty((int) (((Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()))/itemQty) * mixAssort));
											freeList.add(details);
										}

									} else {
										FreeItemDetails details = new FreeItemDetails();
										int itemQty = (int) Float.parseFloat(freeMslab.getFFREEMSLAB_ITEM_QTY());
										details.setRefno(freeHed.getFFREEHED_REFNO());
										details.setFreeIssueSelectedItem(det.getFORDDET_ITEM_CODE());
										// Old 19-08-2016 details.setFreeQty((int) Math.round(entedTotQty / itemQty) * (int) Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()));
										details.setFreeQty((int) (((Float.parseFloat(freeMslab.getFFREEMSLAB_FREE_IT_QTY()))/itemQty) * entedTotQty));
										freeList.add(details);
									}
							}
						}

					}
				}
			}
            }
		}

		return freeList;

	}

	// ---------------------------------------------------------------------------------------------------------


}
