package com.udacity.thefedex87.takemyorder.mock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.Drink;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.models.Ingredient;
import com.udacity.thefedex87.takemyorder.models.Restaurant;
import com.udacity.thefedex87.takemyorder.models.Waiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by federico.creti on 07/06/2018.
 */

public final class PostMockData {
    public static void postMockData(){
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference rootRef = db.getReference("restaurants");

        final ArrayList<Waiter> waiters = getWaitersMockedList();

        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DatabaseReference waiterRef = db.getReference("waiters");
                Random rnd = new Random();
                for(int i = 0; i < 3; i++){
                    Waiter waiter = waiters.get(rnd.nextInt(waiters.size()));
                    waiter.setRestaurantId(dataSnapshot.getKey());
                    waiterRef.push().setValue(waiter);
                    waiters.remove(waiter);
                }

                DatabaseReference menuRef = rootRef.child(dataSnapshot.getKey()).child("menu/foods");

                for (int foodCat = 0; foodCat <= 3; foodCat++) {
                    List<Food> dishes = null;
                    String child = "";
                    switch (foodCat){
                        case 0:
                            dishes = getStarterDishMockedList();
                            child = "starters";
                            break;
                        case 1:
                            dishes = getMainDishMockedList();
                            child = "maindishes";
                            break;
                        case 2:
                            dishes = getSideDishMockedList();
                            child = "sidedishes";
                            break;
                        case 3:
                            dishes = getDessertMockedList();
                            child = "desserts";
                            break;
                    }

                    int min = 2;
                    int rndFood = rnd.nextInt(dishes.size() - min) + min;
                    for (int i = 0; i < rndFood; i++) {
                        Food food = dishes.get(rnd.nextInt(dishes.size()));
                        dishes.remove(food);
                        DatabaseReference foodRef = menuRef.child(child);
                        String newKey = foodRef.push().getKey();
                        foodRef.child(newKey).setValue(food);

                        if(food.getImageName() != null && !food.getImageName().isEmpty()){
                            updloadMealImage(food.getImageName(), newKey);
                        }
                    }
                }

                menuRef = rootRef.child(dataSnapshot.getKey()).child("menu/drinks");
                List<Drink> drinks = getDrinkMockedList();
                int min = 2;
                int rndFood = rnd.nextInt(drinks.size() - min) + min;
                for (int i = 0; i < rndFood; i++) {
                    Drink drink = drinks.get(rnd.nextInt(drinks.size()));
                    drinks.remove(drink);
                    menuRef.push().setValue(drink);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for(Restaurant restaurant : getRestaurantsMockedList()){
            rootRef.push().setValue(restaurant);
        }
    }

    private static void updloadMealImage(String imgName, String key){
        StorageReference mealImagesStorageRef = FirebaseStorage.getInstance().getReference().child("meals_images");
        Uri uri = Uri.parse("android.resource://com.udacity.thefedex87.takemyorder/drawable/" + imgName);
        StorageReference photoRef = mealImagesStorageRef.child(key);
        UploadTask uploadTask = photoRef.putFile(uri);
    }

    private static List<Food> getStarterDishMockedList(){
        //https://food.ndtv.com/food-drinks/10-best-starter-recipes-781678
        //https://www.greatbritishchefs.com/recipes/lobster-thermidor-recipe

        List<Food> foods = new ArrayList<>();

        Food food = new Food();
        food.setName("Kakori Kebab Recipe");
        food.setPrice(7.5);
        food.setImageName("recipe_kachori_kebab");
        food.setDescription("These Lucknowi Kebabs are nothing short of a celebration of meat. Juicy, succulent and just right, spruce them up with some chaat masala, fresh mint chutney, and they’ll have your party off to an impressive start.");
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Mutton/lamb (minced)");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ginger- Garlic paste");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Raw papaya");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cloves");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black cardamom seeds");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cinnamon");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cumin seeds");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Blade mace");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Nutmeg");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Onions");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Bhuna chana");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Egg");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Stir Fried Chilli Chicken");
        food.setPrice(8.7);
        food.setImageName("stir_fried_chilli_chicken");
        food.setDescription("We bring you the best of the best in just 15 minutes! So skip the usual and cook up a storm in your kitchen with this scrumptiously simple chilli chicken.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cloves garlic");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Red and green chillies");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Chicken");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Chilli sauce");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Tomato puree");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Soy sauce");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Sugar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Basil");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Microwave Paneer Tikka Recipe");
        food.setPrice(5);
        food.setImageName("microwave_paneer_tikka");
        food.setDescription("No tandoor? No problem! All you need is a microwave and a few minutes to spare for this flavor-packed paneer tikka.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Paneer ");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Garlic");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ginger ");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Chaat masala");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Chilli powder");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Powdered black pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Vinegar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Lemon");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Onion");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Cheese Balls Recipe");
        food.setPrice(5.5);
        food.setImageName("cheese_balls");
        food.setDescription("No forks or spoons are required for this easy-to-grab party snack. Our outstanding cheese balls are all about the crunch and so sinful yet simple. (Umm, yum!)");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Eggs");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Maida");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Chilli powder");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Baking Powder");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName(" Oil");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Lobster Thermidor");
        food.setPrice(13);
        food.setImageName("lobster_therminador");
        food.setDescription("Geoffrey Smeddle's Lobster Thermidor recipe offers a twist on the Parisian classic, bringing it home to Britain with the use of Anster cheese - a handmade, traditional and local cheese made on the North West coast of Scotland carrying a delicate and rich flavour, similar to that of Wenslydale or a mild English Cheddar. This recipe was developed as part of Geoffrey's collaboration with the Sunday Herald.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Large onion");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Fennel");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Thyme");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Tomato purée");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Dijon mustard");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("White wine");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Brandy");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Lobster stock");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Double cream");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Anster cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Tarragon");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Butter");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Tabasco");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        return foods;
    }

    private static List<Food> getMainDishMockedList(){
        //https://www.allrecipes.com

        List<Food> foods = new ArrayList<>();

        Food food = new Food();
        food.setName("Brodetto (Fish Stew) Ancona-Style");
        food.setPrice(11);
        food.setImageName("brodetto_ancona_style");
        food.setDescription("Brodetto, a fish stew with a tomato base, is a specialty of the Marche region of Italy. There are several recipes for brodetto, even within the Marche. This recipe belongs to the province of Ancona. To make this recipe, I chose fish available in the southern United States");
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Olive oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Onion");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cloves garlic");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Carrot");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Celery ribs");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Bay leaves");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Fresh parsley");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Red pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Tomatoes");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("White vinegar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Fish stock");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Clampa in shell");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Shrimp");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Italian bread");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Spinach Enchiladas");
        food.setPrice(8);
        food.setImageName("spinach_enchiladas");
        food.setDescription("If you like spinach and Mexican food, you'll love these easy vegetarian enchiladas made with ricotta cheese and spinach.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Butter");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Green Onion");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cloves garlic");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Spinach");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ricotta");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Sour cream");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Monterey Jack cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Tortillas");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Enchilada sauce");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Lasagne Alla Bolognese Saporite");
        food.setPrice(9);
        food.setImageName("lasagne_alla_bolognese");
        food.setDescription("This is the classic lasagne alla Bolognese recipe from the Emilia region in Northern Italy. The Bolognese sauce is made with a mixture of beef and pork mince. The addition of prosciutto, red wine, cinnamon, and nutmeg make it truly authentic.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Extra-virgin olive oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Onion");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Carrot");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Shallot");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ham");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ground pork");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ground beef");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ground nutmeg");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ground cinnamon");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("San Marzano tomatoes");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Butter");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Flour");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Milk");
        ingredients.add(ingredient);
        ingredient.setName("Ground nutmeg");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Lasagna noodles");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Parmesan cheese");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Penne Alla Norcina");
        food.setPrice(8.5);
        food.setImageName("penne_alla_norcina");
        food.setDescription("The name Norcina comes from the town of Norcia, famous for cured meat, cheese and the precious truffles.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Penne pasta");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Single cream");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Onion");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Olive oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Pork sausages");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Parmesan/Pecorino");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black truffel");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Nutmeg");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Spaghetti alla Carbonara");
        food.setPrice(7);
        food.setImageName("spaghetti_alla_carbonara");
        food.setDescription("Spaghetti alla Carbonara: When it's good, it can make your eyes roll back in your head with pleasure. It lurks there, beckoning, batting its eyelashes on Italian menus. When you don't order it, you usually end up wishing you had.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Spaghetti");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Olive oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Guanciale (pork cheek");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Eggs");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Pecorino romano");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black pepper");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Pizza margherita");
        food.setPrice(5);
        food.setImageName("pizza_margherita");
        food.setDescription("The original Italian pizza, the queen of Pizza");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Flour");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Water");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Olive oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Yeast");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Tomato souce");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Mozzarella cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Basil");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        return foods;
    }

    private static List<Food> getSideDishMockedList(){
        //https://www.allrecipes.com
        //www.seriouseats.com
        //https://www.justataste.com

        List<Food> foods = new ArrayList<>();

        Food food = new Food();
        food.setName("Boston Baked Beans");
        food.setPrice(7.5);
        food.setImageName("boston_backed_beans");
        food.setDescription("What are Boston baked beans? The short answer is that they're small white beans (usually navy beans), slow-cooked in an oven, hearth, or ember-filled hole in the ground with molasses, salt pork, black pepper, and maybe a touch of mustard and onion until they form a thick stew, rich with a deep color and caramelized crust. Those are the ingredients my 1939 copy of The New England Yankee Cookbook calls for; it's what my 1914 copy of Household Discoveries & Mrs. Curtis's Cook Book describes; and it's what The Fannie Farmer Cookbook instructs as well (along with adding a couple of tablespoons of sugar).");
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Navy beans");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Bacon");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Onion");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Molasses");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Dry mustard");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ketchup");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Worcestershire sauce");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Sugar");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Parmigiana di melanzane");
        food.setPrice(6.5);
        food.setImageName("parmigiana_di_melanzane");
        food.setDescription("An amazin Italian side dish made with aubergine. An incredible taste from one of the most important cuisine in the world.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Aubergine");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Parmesan cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Black pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Peanuts oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Tomato sauce");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Onion");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Basil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Mozzarella cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Olive oil");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Tuscan-Style Fillet of Beef in Green Peppercorn Sauce");
        food.setPrice(12);
        food.setImageName("fillet_of_beef_green_peppercon");
        food.setDescription("It's an incredibly simple and quick—you can have it on the table in 15 to 20 minutes—yet immensely satisfying dish: a melt-in-your-mouth cut of grilled beef tenderloin served with a creamy, piquant pickled green peppercorn sauce.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Beef tenderloin fillets");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Green peppercorns");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Butter");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cognac");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cream");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Lemon Scaloppine");
        food.setPrice(7);
        food.setImageName("lemon_scaloppine");
        food.setDescription("Veal cutlets with lemon juice. This is the way Italian kids are initiated to meat.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Veal cutlets");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("White flour");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Lemon juice");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Butter");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Canola oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Beef broth");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Italian parsley");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Garlic");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ground pepper");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Polpette fritte");
        food.setPrice(7.5);
        food.setImageName("polpette_fritte");
        food.setDescription("There are few things better than homemade meatballs. These Polpette Fritte hail from the northeastern region of Italy and combine spicy Italian sausage, ground turkey and beef with raisins, pine nuts and Parmesan. Roll this killer combo of sweetness and spice in breadcrumbs then saute them until golden brown for the perfect appetizer or addition to your favorite pasta. I enjoyed them tonight with orechiette tossed with garlic-infused olive oil and fresh parm. Pasta has never looked so good.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Spici Italian sausage");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ground beef");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Ground turkey");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Not-fat milk");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Italian-style breadcrumbs");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Garlic");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Parmesan cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Eggs");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Italian flat leaf parsley");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Pine nuts");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Raisins");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Pepper");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Olive oil");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Water");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        return foods;
    }

    private static List<Food> getDessertMockedList(){
        List<Food> foods = new ArrayList<>();

        Food food = new Food();
        food.setName("Lemon sorbet");
        food.setPrice(4);
        food.setImageName("lemon_sorbet");
        food.setDescription("A simple and refreshing lemon sorbet with just 4 ingredients, serve as a light dessert or in between courses at a dinner party");
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Sugar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Lemon");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Water");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Vodka");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Panna cotta");
        food.setPrice(5);
        food.setImageName("panna_cotta");
        food.setDescription("Panna cotta is an Italian dessert of sweetened cream thickened with gelatin and molded. The cream may be aromatized with, coffee, vanilla, or other flavorings.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Envelope unflavored gelatin");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("water");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Heavy cream");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Sugar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Vanilla extract");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Victorian Sponge Cake");
        food.setPrice(6);
        food.setImageName("victorian_sponge_cake");
        food.setDescription("This is the traditional Victoria sponge cake, a much loved English favourite");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Eggs");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Flour");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Sugar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Butter");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Vanilla extract");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Jam");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Red velvet cake");
        food.setPrice(8);
        food.setImageName("red_velvet");
        food.setDescription("A wonderful recipe for the classic American red velvet cake with a white chocolate cream cheese icing.");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Butter");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Sugar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Eggs");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Red food colouring");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Best quality cocoa");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Flour");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Buttermilk");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Vanilla extract");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Bicarbonate of soda");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Vinegar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cream cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("White chocolate");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        food = new Food();
        food.setName("Cheescake");
        food.setPrice(5.5);
        food.setImageName("cheescake");
        food.setDescription("Cheesecake is a sweet dessert consisting of one or more layers");
        ingredients = new ArrayList<>();
        ingredient = new Ingredient();
        ingredient.setName("Cream cheese");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Sugar");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Cornstarch");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Lemon juice");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Vanilla extract");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Eggs");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Graham cracker");
        ingredients.add(ingredient);
        ingredient = new Ingredient();
        ingredient.setName("Butter");
        ingredients.add(ingredient);
        food.setIngredients(ingredients);
        foods.add(food);

        return foods;
    }

    private static List<Drink> getDrinkMockedList(){
        List<Drink> drinks = new ArrayList<>();

        Drink drink = new Drink();
        drink.setName("Water");
        drink.setPrice(2.5);
        drinks.add(drink);

        drink = new Drink();
        drink.setName("Thè");
        drink.setPrice(3.5);
        drinks.add(drink);

        drink = new Drink();
        drink.setName("Coca-Cola");
        drink.setPrice(3.5);
        drink.setImageName("coca_cola");
        drinks.add(drink);

        drink = new Drink();
        drink.setName("Orange juice");
        drink.setPrice(4);
        drink.setImageName("orange_juice");
        drinks.add(drink);

        drink = new Drink();
        drink.setName("Beer");
        drink.setPrice(4.5);
        drink.setImageName("beer");
        drinks.add(drink);

        drink = new Drink();
        drink.setName("Sprite");
        drink.setPrice(4);
        drink.setImageName("sprite");
        drinks.add(drink);

        return drinks;
    }

    private static List<Restaurant> getRestaurantsMockedList(){
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant r = new Restaurant();
        r.setName("Mencuccio");
        r.setLat(43.366700);
        r.setLng(12.237358);
        r.setPlaceId("ChIJO1U5XOENLBMRegbF2LxJGTY");
        restaurants.add(r);

        r = new Restaurant();
        r.setName("Calico Jack");
        r.setLat(43.300405);
        r.setLng(12.341773);
        r.setPlaceId("ChIJx59tgs4QLBMRiFDbJ70U4BQ");
        restaurants.add(r);

        r = new Restaurant();
        r.setName("Wood Pub");
        r.setLat(43.302858);
        r.setLng(12.331823);
        r.setPlaceId("ChIJS2HpwMQQLBMRARDAFYMVVNs");
        restaurants.add(r);

        r = new Restaurant();
        r.setName("Ristorante del sole");
        r.setLat(43.109994);
        r.setLng(12.390159);
        r.setPlaceId("ChIJhY6ND4egLhMROPBqeKhIE_E");
        restaurants.add(r);

        r = new Restaurant();
        r.setName("Ristorante Sottovento");
        r.setLat(43.183221);
        r.setLng(12.137858);
        r.setPlaceId("ChIJtyLHjQoBLBMRt7QRk4K5_DM");
        restaurants.add(r);

        return restaurants;
    }

    private static ArrayList<Waiter> getWaitersMockedList(){
        ArrayList<Waiter> waiters = new ArrayList<>();

        Waiter waiter = new Waiter();
        waiter.setFirstName("Marco");
        waiter.setLastName("Rossi");
        waiter.setUserName("m.rossi22");
        waiter.setPassword("mrossi22pwd");
        waiter.setEmail("mrossi22@fakemail.com");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Luigi");
        waiter.setLastName("Pardi");
        waiter.setUserName("l.pardi45");
        waiter.setPassword("lpardi45pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Bernard");
        waiter.setLastName("Burdi");
        waiter.setUserName("b.burdi56");
        waiter.setPassword("bburdi12pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Federico");
        waiter.setLastName("Taddei");
        waiter.setUserName("f.taddei36");
        waiter.setPassword("ftaddei36pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("John");
        waiter.setLastName("Richard");
        waiter.setUserName("j.richard81");
        waiter.setPassword("jrichard81pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Alex");
        waiter.setLastName("Sarti");
        waiter.setUserName("a.sarti55");
        waiter.setPassword("asarti55pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Ace");
        waiter.setLastName("Ventura");
        waiter.setUserName("a.ventura69");
        waiter.setPassword("aventura69pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Chiara");
        waiter.setLastName("Misciotti");
        waiter.setUserName("c.misciotti70");
        waiter.setPassword("cmisciotti70pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Martina");
        waiter.setLastName("Vanti");
        waiter.setUserName("m.vanti99");
        waiter.setPassword("mvanti99pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Vale");
        waiter.setLastName("Rossi");
        waiter.setUserName("v.rossi46");
        waiter.setPassword("vrossi46pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Jack");
        waiter.setLastName("Sparrow");
        waiter.setUserName("j.sparrow14");
        waiter.setPassword("jsparrow14pwd");
        waiter.setEmail("jsparrow14@fakemail.com");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Franco");
        waiter.setLastName("Franchi");
        waiter.setUserName("f.franchi59");
        waiter.setPassword("ffranchi59pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Megan");
        waiter.setLastName("Miner");
        waiter.setUserName("m.miner23");
        waiter.setPassword("mminer23pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Aldo");
        waiter.setLastName("Zanchi");
        waiter.setUserName("a.zanchi88");
        waiter.setPassword("azanchi88pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Bran");
        waiter.setLastName("Stark");
        waiter.setUserName("b.stark60");
        waiter.setPassword("bstark60pwd");
        waiters.add(waiter);

        return waiters;
    }
}
