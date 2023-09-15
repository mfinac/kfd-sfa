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
    Freeslab(13),
    Freemslab(14),
    Freehed(15),
    Freedet(16),
    Freedeb(17),
    Freeitem(18),
    Reason(19),
    ItemPri(20),
    Area(21),
    Locations(22),
    Towns(23),
    Groups(24),
    Brand(25),
    Cost(26),
    Supplier(27),
    RepDebtor(28),
    Payments(29),
    OrderStatus(30),
    PushMsgHedDet(31),
    CusPRecHed(32),
    CusPRecDet(33),
    RepTrgHed(44),
    RepTrgDet(45),
    invL3hed(46),
    invL3det(47);


    int value;

    private TaskTypeDownload(int value) {
        this.value = value;
    }

}
