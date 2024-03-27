package john.smith.navigationdrawer3;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Apply the saved theme preference
        SharedPreferences preferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        int savedTheme = preferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        //https://stackoverflow.com/questions/72634225/onbackpressed-is-deprecated-what-is-the-alternative

        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle onback pressed
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else {
                    System.exit(0);
                }
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (item.getItemId() == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        } else if (item.getItemId() == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
        } else if (item.getItemId() == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (item.getItemId() == R.id.nav_logout) {
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_theme_toggle) {
            toggleTheme();
            return true;
        } else if (id == R.id.action_search) {
            showSearchDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        // Retrieve the current theme
        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Calculate the new theme
        int newTheme = 0;
                if (currentTheme == Configuration.UI_MODE_NIGHT_NO) {
                    newTheme = AppCompatDelegate.MODE_NIGHT_YES;

                } else {
                    newTheme = AppCompatDelegate.MODE_NIGHT_NO;
                }


        // Apply the new theme
        AppCompatDelegate.setDefaultNightMode(newTheme);

        // Save the new theme preference
        SharedPreferences.Editor editor = getSharedPreferences("ThemePrefs", MODE_PRIVATE).edit();
        editor.putInt("theme", newTheme);
        editor.apply();

        // Recreate the activity to apply the theme changes
        recreate();
    }

    private void showSearchDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_search, null);

        // Find views in the dialog layout
        EditText searchEditText = dialogView.findViewById(R.id.searchEditText);
        Button searchButton = dialogView.findViewById(R.id.searchButton);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create and show the AlertDialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Handle search when the button is clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchQuery = searchEditText.getText().toString();
                // Process the search query as needed
                performSearch(searchQuery);

                // Dismiss the dialog
                dialog.dismiss();

                // Close the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                view.clearFocus();

            }
        });
    }

    private void performSearch(String query) {


        // Handle the search query, e.g., show results in a toast
        Toast.makeText(this, "Search query: " + query, Toast.LENGTH_SHORT).show();
        // Launch Google Search with the user's query
        Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
        searchIntent.putExtra(SearchManager.QUERY, query);
        startActivity(searchIntent);
    }
}