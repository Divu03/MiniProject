package com.littlelemon.fruithub

interface Destination{
    val route:String
}

object MainActivityScreen:Destination{
    override val route = "HomeScreen"
}

object ExploreScreen:Destination{
    override val route = "ExploreScreen"
}
object MySaveScreen:Destination{
    override val route = "MySaveScreen"
}
object CameraScreenDestination:Destination{
    override val route = "CameraScreen"
}
object UserScreen:Destination{
    override val route = "UserScreen"
}
