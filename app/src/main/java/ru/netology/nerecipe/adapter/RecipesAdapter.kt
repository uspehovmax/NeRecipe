package ru.netology.nerecipe.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.RecipeBinding
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.data.Recipe

class RecipesAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RecipeBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveButtonClicked(recipe)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditButtonClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.options.setOnClickListener { popupMenu.show() }
        }

        init {
            binding.authorName.setOnClickListener { listener.onRecipeCardClicked(recipe) } //
            binding.title.setOnClickListener { listener.onRecipeCardClicked(recipe) } //
            binding.dishPicture.setOnClickListener { listener.onRecipeCardClicked(recipe) } //
            binding.recipeDescription.setOnClickListener { listener.onRecipeCardClicked(recipe) } //
        }

        init {
            itemView.setOnClickListener { listener.onRecipeItemClicked(recipe) }
            binding.favorite.setOnClickListener { listener.onLikesButtonClicked(recipe) }
            binding.share.setOnClickListener { listener.onShareButtonClicked(recipe) }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe // из lateinit

            with(binding) {
                title.text = recipe.title
                authorName.text = recipe.author
                region.text = region.context.showRegion(recipe.region)
                favorite.isChecked = recipe.likedByMe
                favorite.text = recipe.likes.toString()
                share.text = recipe.shareCount.toString()
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
    }
}

fun Context.showRegion(region: Region): String {
    return when (region) {
        Region.European -> getString(R.string.european)
        Region.Asian -> getString(R.string.asian)
        Region.PanAsian -> getString(R.string.panasian)
        Region.Eastern -> getString(R.string.eastern)
        Region.American -> getString(R.string.american)
        Region.Russian -> getString(R.string.russian)
        Region.Mediterranean -> getString(R.string.mediterranean)
    }
}


