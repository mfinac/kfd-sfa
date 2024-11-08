package com.datamation.kfdupgradesfa.settings;


public enum TaskTypeDownload {

    fddbnote(1),
    Controllist(2),
    SalRep(3),
    Customers(4),
    Settings(5),
    Reference(6),
    Items(7),
    ItemLoc(8),
    Type(9),
    Bank(10),
    Route(11),
    RouteDet(12),
    //Freeslab(13),
    Freemslab(13),
    Freehed(14),
    Freedet(15),
    Freedeb(16),
    Freeitem(17),
    Reason(18),
    ItemPri(19),
    Area(20),
    Locations(21),
    Towns(22),
    Groups(23),
    Brand(24),
    Cost(25),
    Supplier(26),
    RepDebtor(27),
//    Payments(29),
//    OrderStatus(30),
//    PushMsgHedDet(31),
//    CusPRecHed(32),
//    CusPRecDet(33),
//    RepTrgHed(44),
//    RepTrgDet(45),
    invL3hed(28),
    invL3det(29);


    int value;

    private TaskTypeDownload(int value) {
        this.value = value;
    }

}
