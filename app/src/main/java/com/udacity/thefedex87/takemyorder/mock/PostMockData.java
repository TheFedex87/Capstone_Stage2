package com.udacity.thefedex87.takemyorder.mock;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.thefedex87.takemyorder.model.Food;
import com.udacity.thefedex87.takemyorder.model.Ingredient;
import com.udacity.thefedex87.takemyorder.model.Restaurant;
import com.udacity.thefedex87.takemyorder.model.Waiter;

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

    private static List<Food> getStarterDishMockedList(){
        //https://food.ndtv.com/food-drinks/10-best-starter-recipes-781678
        //https://www.greatbritishchefs.com/recipes/lobster-thermidor-recipe

        List<Food> foods = new ArrayList<>();

        Food food = new Food();
        food.setName("Kakori Kebab Recipe");
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

    private List<Food> getMainDishMockedList(){
        //https://www.allrecipes.com

        List<Food> foods = new ArrayList<>();

        Food food = new Food();
        food.setName("Brodetto (Fish Stew) Ancona-Style");
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

        return foods;
    }

    private static List<Restaurant> getRestaurantsMockedList(){
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant r = new Restaurant();
        r.setName("Enjoy Food");
        r.setLat(43.340191);
        r.setLng(12.294784);
        restaurants.add(r);

        r = new Restaurant();
        r.setName("MWP");
        r.setLat(43.350438);
        r.setLng(12.287428);
        restaurants.add(r);

        r = new Restaurant();
        r.setName("Eat Eat Eat");
        r.setLat(43.303262);
        r.setLng(12.335086);
        restaurants.add(r);

        r = new Restaurant();
        r.setName("Happy meal");
        r.setLat(43.108486);
        r.setLng(12.387825);
        restaurants.add(r);

        r = new Restaurant();
        r.setName("IWTWIWT");
        r.setLat(43.101909);
        r.setLng(12.310238);
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
