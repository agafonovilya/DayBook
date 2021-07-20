package ru.agafonovilya.daybook.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.agafonovilya.daybook.Injection
import ru.agafonovilya.daybook.databinding.FragmentHomeBinding
import ru.agafonovilya.daybook.model.entity.Homework
import ru.agafonovilya.daybook.model.entity.Lesson
import ru.agafonovilya.daybook.view.adapter.HomeworkListRvAdapter
import ru.agafonovilya.daybook.view.adapter.LessonsRvAdapter
import ru.agafonovilya.daybook.viewmodel.HomeViewModel

class HomeFragment: Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var timerJob: Job? = null

    private lateinit var lessonsAdapter : LessonsRvAdapter
    private lateinit var homeworkListAdapter : HomeworkListRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initRecyclerView()
        getHomeworkList()
        getLessonList()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(this)
        ).get(HomeViewModel::class.java)
    }

    private fun getHomeworkList() {
        lifecycleScope.launch {
            viewModel.loadHomeworkList().collect {
                homeworkListAdapter.homeworkList = it
            }
        }
    }

    private fun getLessonList() {
        lifecycleScope.launch {
            viewModel.loadLessonList().collect {
                lessonsAdapter.lessonList = it
                binding.rvLessons.scrollToPosition(viewModel.findCurrentTimeLessonPosition(it))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        timerJob = lifecycleScope.launchWhenStarted {
            viewModel.getExamsDate().collect {
                updateCountDown(it)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        timerJob?.cancel()
    }

     private fun initRecyclerView(){
         binding.rvLessons.layoutManager = LinearLayoutManager(
             requireContext(),
             LinearLayoutManager.HORIZONTAL,
             false)
         lessonsAdapter = LessonsRvAdapter(onLessonItemClickListener(), openSkype())
         binding.rvLessons.adapter = lessonsAdapter

         binding.rvHomework.layoutManager = LinearLayoutManager(
             requireContext(),
             LinearLayoutManager.HORIZONTAL,
             false)
         homeworkListAdapter = HomeworkListRvAdapter(onHomeworkItemClickListener())
         binding.rvHomework.adapter = homeworkListAdapter

    }

    private fun onHomeworkItemClickListener(): (Homework) -> Unit  = {
        showToast(it.lesson)
    }

    private fun onLessonItemClickListener(): (Lesson) -> Unit  = {
        showToast(it.title)
    }

    private fun openSkype(): (Lesson) -> Unit  =  {
        val intent: Intent? = requireActivity().packageManager?.getLaunchIntentForPackage("com.skype.raider")
        if (intent != null) {
            startActivity(intent)
        } else {
            showToast("Skype not found")
        }
    }

    private fun updateCountDown(countDownTime: Long) {
        val countDownTimeMinutes = countDownTime / 60000 //Получаем время в минутах
        val days = (countDownTimeMinutes / 1440)
        val hours = (countDownTimeMinutes - (days * 1440))/60
        val minutes = ((countDownTimeMinutes - (days * 1440) -(hours * 60)))

        binding.containerCountdown.tvDays1.text = (days / 10).toString()
        binding.containerCountdown.tvDays2.text = (days % 10).toString()

        binding.containerCountdown.tvHours1.text = (hours / 10).toString()
        binding.containerCountdown.tvHours2.text = (hours % 10).toString()

        binding.containerCountdown.tvMinutes1.text = (minutes / 10).toString()
        binding.containerCountdown.tvMinutes2.text = (minutes % 10).toString()

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}