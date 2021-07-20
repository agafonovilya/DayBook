package ru.agafonovilya.daybook.view


sealed class Screens {

    class HomeScreen {
        fun getFragment() = HomeFragment.newInstance()
    }

    class ScheduleScreen {
        fun getFragment() = ScheduleFragment.newInstance()
    }

    class NotesScreen() {
    }

    class SelectedScreen() {
    }
}