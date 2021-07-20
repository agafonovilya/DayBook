package ru.agafonovilya.daybook.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.agafonovilya.daybook.Injection
import ru.agafonovilya.daybook.R
import ru.agafonovilya.daybook.databinding.ScheduleLessonItemBinding
import ru.agafonovilya.daybook.databinding.ScheduleLessonOptionalItemBinding
import ru.agafonovilya.daybook.model.entity.Lesson

class ScheduleRvAdapter(
    private val onItemClickListener: (Lesson) -> Unit,
    private val onSkypeClickListener: (Lesson) -> Unit
) : RecyclerView.Adapter<ScheduleRvAdapter.ScheduleViewHolder>() {

    var lessonList: List<Lesson> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view: View = when (viewType) {
            2 -> LayoutInflater.from(parent.context)
                .inflate(R.layout.schedule_lesson_optional_item, parent, false)
            else -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.schedule_lesson_item, parent, false)
            }
        }
        return ScheduleViewHolder(
            view,
            onItemClickListener,
            onSkypeClickListener
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (lessonList[position].isOptional) 2 else 1
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(lessonList[position])
    }

    override fun getItemCount() = lessonList.size

    inner class ScheduleViewHolder(
        itemView: View,
        private val onItemClickListener: (Lesson) -> Unit,
        private val onSkypeClickListener: (Lesson) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val imageLoader = Injection.provideImageLoader()
        private val appResources = Injection.provideAppResources()

        fun bind(lesson: Lesson) {
            when (itemView.tag) {

                appResources.getString(R.string.schedule_lesson_item) -> {

                    val binding = ScheduleLessonItemBinding.bind(itemView)
                    binding.tvTitle.text = lesson.title
                    binding.tvTime.text = ("${lesson.timeStart} - ${lesson.timeEnd}")
                    imageLoader.loadInto(lesson.image, binding.ivImage)

                    if (lesson.isOnline) {
                        binding.openSkypeLayout.visibility = View.VISIBLE
                        binding.openSkypeLayout.setOnClickListener {
                            onSkypeClickListener(lesson)
                        }
                    }

                    itemView.setOnClickListener {
                        onItemClickListener(lesson)
                    }
                }

                appResources.getString(R.string.schedule_lesson_optional_item) -> {

                    val binding = ScheduleLessonOptionalItemBinding.bind(itemView)
                    binding.tvTitle.text = lesson.title
                    binding.tvTime.text = ("${lesson.timeStart} - ${lesson.timeEnd}")
                    imageLoader.loadInto(lesson.image, binding.ivImage)

                    lesson.optionalDescription?.let {
                        binding.tvDescription.text = it
                    }

                    if (lesson.isOnline) {
                        binding.openSkypeLayout.visibility = View.VISIBLE
                        binding.openSkypeLayout.setOnClickListener {
                            onSkypeClickListener(lesson)
                        }
                    }

                    itemView.setOnClickListener {
                        onItemClickListener(lesson)
                    }
                }
            }
        }
    }
}