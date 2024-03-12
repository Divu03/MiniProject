package com.littlelemon.fruithub

interface Destination{
    val route:String
}

object MainActivityScreen:Destination{
    override val route = "MainActivity"
}

object ExploreScreen:Destination{
    override val route = "Explore"
}
object MySaveScreen:Destination{
    override val route = "MySave"
}
