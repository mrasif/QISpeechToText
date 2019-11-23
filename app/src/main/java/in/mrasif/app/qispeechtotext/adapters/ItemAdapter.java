package in.mrasif.app.qispeechtotext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.mrasif.app.qispeechtotext.R;
import in.mrasif.app.qispeechtotext.databinding.ItemWordBinding;
import in.mrasif.app.qispeechtotext.models.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context context;
    private List<Item> items;

    public ItemAdapter(Context context) {
        this.context = context;
        items=new ArrayList<>();
    }

    public void update(List<Item> items){
        this.items=items;
        Collections.sort(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_word,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item=items.get(position);
        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemWordBinding binding;
        public ViewHolder(ItemWordBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
