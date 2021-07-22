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
) : RecyclerView.Adapter<ScheduleRvAdapter.ScheduleViewHolderAbstract>() {

    var lessonList: List<Lesson> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolderAbstract {
        return createScheduleViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (lessonList[position].isOptional) 2 else 1
    }

    override fun onBindViewHolder(holder: ScheduleViewHolderAbstract, position: Int) {
        holder.bind(lessonList[position])
    }

    override fun getItemCount() = lessonList.size

    fun createScheduleViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolderAbstract {
        when (viewType) {
            2 -> {
                val binding = ScheduleLessonOptionalItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ScheduleOptionalViewHolder(
                    binding,
                    onItemClickListener,
                    onSkypeClickListener
                )
            }
            else -> {
                val binding = ScheduleLessonItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ScheduleMandatoryViewHolder(
                    binding,
                    onItemClickListener,
                    onSkypeClickListener
                )
            }
        }
    }

    abstract class ScheduleViewHolderAbstract(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        protected val imageLoader = Injection.provideImageLoader()

        abstract fun bind(lesson: Lesson)
    }

    inner class ScheduleMandatoryViewHolder(
        private val binding: ScheduleLessonItemBinding,
        private val onItemClickListener: (Lesson) -> Unit,
        private val onSkypeClickListener: (Lesson) -> Unit
    ) : ScheduleViewHolderAbstract(binding.root) {

        override fun bind(lesson: Lesson) {
            binding.tvTitle.text = lesson.title
            binding.tvTime.text = ("${lesson.timeStart} - ${lesson.timeEnd}")
            imageLoader.loadInto(lesson.image, binding.ivImage)

            if (lesson.isOnline) {
                binding.openSkypeLayout.visibility = View.VISIBLE
                binding.openSkypeLayout.setOnClickListener {
                    onSkypeClickListener(lesson)
                }
            }

            binding.root.setOnClickListener {
                onItemClickListener(lesson)
            }
        }
    }

    inner class ScheduleOptionalViewHolder(
        private val binding: ScheduleLessonOptionalItemBinding,
        private val onItemClickListener: (Lesson) -> Unit,
        private val onSkypeClickListener: (Lesson) -> Unit
    ) : ScheduleViewHolderAbstract(binding.root) {

        override fun bind(lesson: Lesson) {
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