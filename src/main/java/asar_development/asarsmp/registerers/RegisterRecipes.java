package asar_development.asarsmp.registerers;

import asar_development.asarsmp.AsarSMP;
import asar_development.asarsmp.managers.filemanager.FileID;
import asar_development.util.Item;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class RegisterRecipes {
    public RegisterRecipes() { run(); }

    public void run() {
        final long start = System.currentTimeMillis();
        var instance = AsarSMP.getInstance();
        ConfigurationSection recipesFile = instance.getFileManager().getFile(FileID.RECIPES).getFileConfiguration();
        Map<String, Object> sec = recipesFile.getValues(false);
        var recipeIterator = Bukkit.recipeIterator();
        List<NamespacedKey> l = new ArrayList<>();
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if ((recipe instanceof Keyed keyedRecipe) && "minecraft".equals(keyedRecipe.getKey().getNamespace())) {
                l.add(keyedRecipe.getKey());
            }
        }
        for (NamespacedKey key : l) {
            Bukkit.removeRecipe(key);
        }
        for (String path : sec.keySet()) {
            if (!recipesFile.getBoolean(path + ".enabled")) {
                continue;
            }
            var resultItemID = recipesFile.getString(path + ".result", path);
            var shapeless = recipesFile.getBoolean(path + ".shapeless", false);
            ItemStack item = Item.getItem(resultItemID);
            if ("air".equals(resultItemID)) {
                item = new ItemStack(Material.AIR);
            }
            Recipe recipe;
            if (shapeless) {
                recipe = registerShapelessRecipe(path, item);
            } else {
                recipe = registerShapedRecipe(path, item);
            }
            // Finally, add the recipe to the bukkit recipes
            Bukkit.addRecipe(recipe);
        }
        String logMessage = String.format("Registered all recipes without any critical errors! (%dms)", System.currentTimeMillis() - start);
        instance.getLogger().log(Level.INFO, logMessage);
    }
    Recipe registerShapedRecipe(String path, @NotNull ItemStack item) {
        var instance = AsarSMP.getInstance();
        ConfigurationSection recipesFile = instance.getFileManager().getFile(FileID.RECIPES).getFileConfiguration();
        var key = new NamespacedKey(instance, path);
        item.setAmount(recipesFile.getInt(path + ".result_amount", 0));
        var recipe = new ShapedRecipe(key, item);
        recipe.shape("ABC", "DEF", "GHJ");
        var str = "ABCDEFGHJ";
        for (var i = 1; i < 10; i++) {
            var ingItemID = recipesFile.getString(path + ".recipe." + i);
            var ingItem = Item.getItem(ingItemID);
            if ("air".equals(ingItemID)) {
                ingItem = new ItemStack(Material.AIR);
            }
            recipe.setIngredient(str.charAt(i - 1), new RecipeChoice.ExactChoice(ingItem));
        }
        return recipe;
    }
    Recipe registerShapelessRecipe(String path, ItemStack item) {
        var instance = AsarSMP.getInstance();
        ConfigurationSection recipesFile = instance.getFileManager().getFile(FileID.RECIPES).getFileConfiguration();
        var key = new NamespacedKey(instance, path);
        var recipe = new ShapelessRecipe(key, item);
        for (var i = 1; i < 10; i++) {
            var ingItemID = recipesFile.getString(path + ".recipe." + i);
            if ("air".equals(ingItemID)) {
                continue;
            }
            if (ingItemID == null) {
                var message = String.format("Item missing in recipes! %s > recipe > %s", path, i);
                instance.getLogger().info(message);
            } else {
                var ingItem = Item.getItem(ingItemID);
                recipe.addIngredient(new RecipeChoice.ExactChoice(ingItem));
            }
        }
        return recipe;
    }
}
