package com.exfaust.feature__cinema_list.ui.all_cinemas

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exfaust.base__cinema.CinemaId
import com.exfaust.core.rx.invoke
import com.exfaust.core_android.getRxAsyncDiffer
import com.exfaust.core_android.layoutInflater
import com.exfaust.feature__cinema_list.data.model.Cinema
import com.exfaust.feature__cinema_list.databinding.CinemaListAllItemBinding
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber

class CinemaListAllAdapter :
    ListAdapter<Cinema, CinemaListAllAdapter.ViewHolder>(
        getRxAsyncDiffer(object : DiffUtil.ItemCallback<Cinema>() {
            override fun areItemsTheSame(
                oldItem: Cinema,
                newItem: Cinema
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Cinema,
                newItem: Cinema
            ): Boolean =
                oldItem == newItem
        })
    ) {

    private val _onItemClick: PublishProcessor<CinemaId> = PublishProcessor.create()
    val onItemClick: Flowable<CinemaId> get() = _onItemClick.hide()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CinemaListAllItemBinding.inflate(parent.context.layoutInflater, parent, false),
            _onItemClick
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val _binding: CinemaListAllItemBinding,
        private val _onItemClick: Subscriber<CinemaId>
    ) : RecyclerView.ViewHolder(
        _binding.root
    ) {
        fun bind(item: Cinema) {
            itemView.apply {
                setOnClickListener { _onItemClick(item.id) }

                _binding.cinemaListAllItemTitle.text = item.name
                _binding.cinemaListAllItemAddress.text = item.address
            }
        }
    }
}
