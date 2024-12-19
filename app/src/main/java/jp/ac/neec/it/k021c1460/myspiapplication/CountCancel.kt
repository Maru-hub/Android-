package jp.ac.neec.it.k021c1460.myspiapplication

var isCancel = false

class CountCancel {

    fun setState(boolean: Boolean){
        isCancel = boolean
    }

    fun getState():Boolean{
        return isCancel
    }
}