package com.example.netflix_ingrid;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        // Inicia com o Splash Screen
        setContentView(R.layout.activity_main);
        
        // Navega para a Home após 2.5 segundos
        new Handler(Looper.getMainLooper()).postDelayed(this::showHome, 2500);
    }

    private void setupBottomNavigation(int activeTabId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(activeTabId);
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home && activeTabId != R.id.nav_home) {
                    showHome();
                    return true;
                } else if (itemId == R.id.nav_rooms && activeTabId != R.id.nav_rooms) {
                    showLobby();
                    return true;
                } else if (itemId == R.id.nav_profile && activeTabId != R.id.nav_profile) {
                    showProfile();
                    return true;
                }
                return false;
            });
        }
    }

    private void showHome() {
        setContentView(R.layout.inicio);
        setupBottomNavigation(R.id.nav_home);
        
        View btnSala = findViewById(R.id.btn_sala_virtual);
        if (btnSala != null) {
            btnSala.setOnClickListener(v -> showLobby());
        }

        View btnSalaPresencial = findViewById(R.id.btn_sala_presencial);
        if (btnSalaPresencial != null) {
            btnSalaPresencial.setOnClickListener(v -> showLobbyPresencial());
        }

        View tvSeries = findViewById(R.id.tv_series);
        if (tvSeries != null) {
            tvSeries.setOnClickListener(v -> showSeriesCatalog());
        }

        View tvFilmes = findViewById(R.id.tv_filmes);
        if (tvFilmes != null) {
            tvFilmes.setOnClickListener(v -> showMoviesCatalog());
        }

        View tvMinhaLista = findViewById(R.id.tv_minha_lista);
        if (tvMinhaLista != null) {
            tvMinhaLista.setOnClickListener(v -> showMyList());
        }
    }

    private void showLobby() {
        setContentView(R.layout.salavirtualinicio);
        setupBottomNavigation(R.id.nav_rooms);
        
        View btnEnter = findViewById(R.id.btn_enter_room);
        if (btnEnter != null) {
            btnEnter.setOnClickListener(v -> showPlayer());
        }
    }

    private void showLobbyPresencial() {
        setContentView(R.layout.salapresencialinicio);
        setupBottomNavigation(R.id.nav_home);
        
        View btnCreate = findViewById(R.id.btn_enter_room_presencial);
        if (btnCreate != null) {
            btnCreate.setOnClickListener(v -> showCreateRoomDialog());
        }
    }

    private void showCreateRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Criar Sala Presencial");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etLocal = new EditText(this);
        etLocal.setHint("Nome do Local (ex: Prédio B)");
        layout.addView(etLocal);

        final EditText etDesc = new EditText(this);
        etDesc.setHint("O que estão assistindo?");
        layout.addView(etDesc);

        builder.setView(layout);

        builder.setPositiveButton("Criar", (dialog, which) -> {
            String local = etLocal.getText().toString();
            String desc = etDesc.getText().toString();
            if (!local.isEmpty() && !desc.isEmpty()) {
                addNewPresencialRoom(local, desc);
            }
        });
        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }

    private void addNewPresencialRoom(String local, String desc) {
        LinearLayout container = findViewById(R.id.container_salas_presenciais);
        if (container == null) return;

        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(80));
        cardParams.setMargins(0, 0, 0, dpToPx(8));
        card.setLayoutParams(cardParams);
        card.setCardBackgroundColor(Color.parseColor("#1F1F1F"));
        card.setRadius(dpToPx(4));

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        relativeLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        TextView tvTitle = new TextView(this);
        tvTitle.setText(local + " - " + desc);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        relativeLayout.addView(tvTitle);

        TextView tvStatus = new TextView(this);
        tvStatus.setText("1 pessoa no local");
        tvStatus.setTextColor(Color.parseColor("#808080"));
        tvStatus.setTextSize(12);
        RelativeLayout.LayoutParams statusParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        statusParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tvStatus.setLayoutParams(statusParams);
        relativeLayout.addView(tvStatus);

        ImageButton btnDelete = new ImageButton(this);
        RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(
                dpToPx(32), dpToPx(32));
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        btnDelete.setLayoutParams(deleteParams);
        btnDelete.setImageResource(android.R.drawable.ic_menu_delete);
        btnDelete.setBackground(null);
        btnDelete.setColorFilter(Color.parseColor("#E50914"));
        btnDelete.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Excluir Sala")
                    .setMessage("Tem certeza que deseja excluir esta sala?")
                    .setPositiveButton("Sim", (dialog, which) -> container.removeView(card))
                    .setNegativeButton("Não", null)
                    .show();
        });
        relativeLayout.addView(btnDelete);

        card.addView(relativeLayout);
        container.addView(card, 0); // Adiciona no topo
        
        card.setOnClickListener(v -> showPlayerPresencial());
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void showPlayerPresencial() {
        setContentView(R.layout.sala_presencial);
        View btnBack = findViewById(R.id.btn_back_player_presencial);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> showLobbyPresencial());
        }
    }

    private void showProfile() {
        setContentView(R.layout.perfil);
        setupBottomNavigation(R.id.nav_profile);
        
        View btnBack = findViewById(R.id.btn_back_profile);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> showHome());
        }
    }

    private void showMoviesCatalog() {
        setContentView(R.layout.filmes);
        View btnBack = findViewById(R.id.btn_back_movies);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> showHome());
        }
    }

    private void showSeriesCatalog() {
        setContentView(R.layout.series);
        View btnBack = findViewById(R.id.btn_back_series);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> showHome());
        }
    }

    private void showPlayer() {
        setContentView(R.layout.sala_virtual);
        View btnBack = findViewById(R.id.btn_back_player);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> showLobby());
        }
    }

    private void showMyList() {
        setContentView(R.layout.minha_lista);
        View btnBack = findViewById(R.id.btn_back_my_list);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> showHome());
        }
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.video_container) != null) {
            showLobby();
        } else if (findViewById(R.id.video_container_presencial) != null) {
            showLobbyPresencial();
        } else if (findViewById(R.id.tv_title) != null || // Lobby Virtual
                   findViewById(R.id.tv_title_presencial) != null || // Lobby Presencial
                   findViewById(R.id.tv_profile_title) != null || // Profile
                   findViewById(R.id.tv_movies_title) != null || // Movies
                   findViewById(R.id.tv_my_list_title) != null || // My List
                   findViewById(R.id.tv_series_title) != null) { // Series
            showHome();
        } else if (findViewById(R.id.logo) != null) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}