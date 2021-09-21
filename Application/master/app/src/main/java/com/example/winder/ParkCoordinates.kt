package com.example.winder

import java.util.ArrayList

class ParkCoordinates {
    var parkCoordinates : HashMap<String, ArrayList<Double>> = HashMap<String, ArrayList<Double>>()
    constructor() {
        fillHashMap()
    }

    private fun fillHashMap() {

        parkCoordinates.put("Røyrmyra", arrayListOf(5.727425, 58.590559))
        parkCoordinates.put("Skomakerfjellet", arrayListOf(10.382294, 64.217132))
        parkCoordinates.put("Rye Vind", arrayListOf(10.118075, 63.418161))
        parkCoordinates.put("Egersund", arrayListOf(6.084107, 58.426187))
        parkCoordinates.put("Svåheia", arrayListOf(6.087737, 58.38343))
        parkCoordinates.put("Tindafjellet", arrayListOf(5.948112, 58.730357))
        parkCoordinates.put("Skurvenuten", arrayListOf(5.907947, 58.749431))
        parkCoordinates.put("Hitra 2", arrayListOf(8.768181, 63.501162))
        parkCoordinates.put("Storøy", arrayListOf(5.226863, 59.415853))
        parkCoordinates.put("Valsneset testpark", arrayListOf(9.629961, 63.819792))
        parkCoordinates.put("Raggovidda", arrayListOf(29.094519, 70.769057))
        parkCoordinates.put("Tellenes", arrayListOf(6.435692, 58.343587))
        parkCoordinates.put("Hamnefjell", arrayListOf(29.711017, 70.672109))
        parkCoordinates.put("Roan", arrayListOf(10.298654, 64.139221))
        parkCoordinates.put("Raskiftet", arrayListOf(11.802568, 61.199542))
        parkCoordinates.put("Ånstadblåheia", arrayListOf(15.259676, 68.724023))
        parkCoordinates.put("Marker", arrayListOf(11.7277017, 59.4870358))
        parkCoordinates.put("Lindesnes", arrayListOf(7.0894, 58.0077))
        parkCoordinates.put("Storheia", arrayListOf(10.159199, 63.879271))
        parkCoordinates.put("Valsneset vindkraftverk", arrayListOf(9.629961, 63.819792))
        parkCoordinates.put("Kvitfjell", arrayListOf(18.133605, 69.588058))
        parkCoordinates.put("Åsen II", arrayListOf(5.753568, 58.737012))
        parkCoordinates.put("Midtfjellet", arrayListOf(5.379783, 59.929296))
        parkCoordinates.put("Mehuken", arrayListOf(5.002993, 62.015344))

    }
}
