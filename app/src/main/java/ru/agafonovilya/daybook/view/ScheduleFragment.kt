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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.agafonovilya.daybook.Injection
import ru.agafonovilya.daybook.databinding.FragmentScheduleBinding
import ru.agafonovilya.daybook.model.entity.Lesson
import ru.agafonovilya.daybook.view.adapter.ScheduleRvAdapter
import ru.agafonovilya.daybook.viewmodel.ScheduleViewModel

class ScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private lateinit var viewModel: ScheduleViewModel
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var scheduleRvAdapter: ScheduleRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initRecyclerView()
        getLesson()
    }

    private fun getLesson() {
        lifecycleScope.launch {
            viewModel.loadLessonList().collect {
                scheduleRvAdapter.lessonList = it
                binding.rvSchedule.scrollToPosition(viewModel.findCurrentTimeLessonPosition(it))
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(this)
        ).get(ScheduleViewModel::class.java)
    }

    private fun initRecyclerView() {
        binding.rvSchedule.layoutManager = LinearLayoutManager(requireContext())
        scheduleRvAdapter = ScheduleRvAdapter(onLessonItemClickListener(), openSkype())
        binding.rvSchedule.adapter = scheduleRvAdapter
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}