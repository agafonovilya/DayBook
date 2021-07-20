package ru.agafonovilya.daybook.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.agafonovilya.daybook.Injection
import ru.agafonovilya.daybook.R
import ru.agafonovilya.daybook.databinding.ItemLessonBinding
import ru.agafonovilya.daybook.model.entity.Lesson

class LessonsRvAdapter(
    private val onItemClickListener: (Lesson) -> Unit,
    private val onSkypeClickListener: (Lesson) -> Unit
) :
    RecyclerView.Adapter<LessonsRvAdapter.LessonsViewHolder>() {

    var lessonList: List<Lesson> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        //Устанавливаю ширину элемента = ширине экрана (если сделать это в xml, потеряется margin между элементами)
        view.layoutParams.width = parent.width
        return LessonsViewHolder(view, onItemClickListener, onSkypeClickListener)
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        holder.bind(lessonList[position])
    }

    override fun getItemCount() = lessonList.size

    inner class LessonsViewHolder(
        itemView: View,
        private val onItemClickListener: (Lesson) -> Unit,
        private val onSkypeClickListener: (Lesson) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ItemLessonBinding.bind(itemView)
        private val imageLoader = Injection.provideImageLoader()

        fun bind(lesson: Lesson) {
            binding.tvTitle.text = lesson.title
            binding.tvTime.text =  ("${lesson.timeStart} - ${lesson.timeEnd}")
            imageLoader.loadInto(lesson.image, binding.ivImage)

            if (lesson.isOnline) {
                binding.openSkypeLayout.visibility = VISIBLE
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