package com.example.opsc_poe

class Temp_CategoryDataClass(
    var categoryID: Int = 0,
    var userID: Int = 0,
    var name: String = "",
    var colour: String = "",
    var description: String = ""
)
{
    public fun GetIndex(id: Int, categorites: ArrayList<Temp_CategoryDataClass>): Int
    {
        var index: Int = -1
        for (i in 0..categorites.size)
        {
            if (categorites[i].categoryID == id)
            {
                index = i;
            }
        }
        return  index
    }
}

