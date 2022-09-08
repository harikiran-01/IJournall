package bliss.platform.android.components.android

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import bliss.platform.android.extensions.getItemInList
import java.util.*

abstract class BaseRecyclerAdapter<T : BaseAdapterViewType> :
    RecyclerView.Adapter<DataBoundViewHolder<ViewDataBinding>>() {

    companion object {
        private const val TAG = "BaseRecyclerAdapter"
    }

    var dataList = ArrayList<T>()
        private set
    private val supportedViewBinderResolverMap = SparseArray<ViewDataBinder<ViewDataBinding, T>>()

    protected abstract fun getSupportedViewDataBinder(): List<ViewDataBinder<ViewDataBinding, T>>

    protected fun initViewDataBinders() {
        getSupportedViewDataBinder().forEach { viewDataBinder ->
            supportedViewBinderResolverMap.put(viewDataBinder.viewType, viewDataBinder)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBoundViewHolder<ViewDataBinding> {
        val viewDataBinder = supportedViewBinderResolverMap.get(viewType)
        if (viewDataBinder == null) {
            val message =
                String.format(Locale.US, "No view binder found for viewType: %d", viewType)
            throw IllegalArgumentException(message)
        }
        return DataBoundViewHolder(viewDataBinder.createBinder(parent))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ViewDataBinding>, position: Int) {
        val viewDataBinder = supportedViewBinderResolverMap.get(getItemViewType(position))
        if (viewDataBinder == null) {
            Log.e(TAG, "Please add the supported view binder to view binders list in adapter")
            return
        }
        viewDataBinder.bindData(holder.binding, dataList[position], position, holder.adapterPosition)
        holder.binding.executePendingBindings()
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<ViewDataBinding>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val viewDataBinder = supportedViewBinderResolverMap.get(getItemViewType(position))
            if (viewDataBinder == null) {
                Log.e(TAG, "Please add the supported view binder to view binders list in adapter")
                return
            }
            val bundle = payloads[0] as? Bundle
            viewDataBinder.bindData(holder.binding, bundle, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= 0 && position < dataList.size) {
            return dataList[position].viewType
        }
        return ITEM_NONE
    }

    override fun getItemCount() = dataList.size

    operator fun get(position: Int): BaseAdapterViewType? {
        return dataList.getItemInList(position)
    }

    operator fun <T> get(position: Int): T? {
        return dataList.getItemInList(position) as? T
    }

    fun setItems(items: List<T>) {
        if (items.isNotEmpty()) {
            dataList = items as ArrayList<T>
            notifyDataSetChanged()
        }
        else
            clear()
    }

    fun update(pos: Int, item: T) {
        dataList[pos] = item
        notifyItemChanged(pos)
    }

    fun addItems(items: List<T>) {
        if (items.isNotEmpty()) {
            val start = dataList.size
            dataList.addAll(items as ArrayList<T>)
            notifyItemRangeInserted(start, dataList.size)
        }
        else
            clear()
    }

    fun addItem(item: T) {
        dataList.add(item)
        notifyItemInserted(dataList.size - 1)
    }

    fun setItem(item: T) {
        dataList.clear()
        dataList.add(item)
        notifyDataSetChanged()
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

}