package rs.etf.pmu.cats.fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;
import rs.etf.pmu.cats.db.entities.components.chassis.TankChassis;
import rs.etf.pmu.cats.db.entities.components.slots.Slot;
import rs.etf.pmu.cats.db.entities.components.slots.UtilitySlot;
import rs.etf.pmu.cats.db.entities.components.slots.WeaponSlot;
import rs.etf.pmu.cats.db.entities.components.slots.WheelsSlot;
import rs.etf.pmu.cats.db.entities.components.weapons.Blade;
import rs.etf.pmu.cats.db.entities.components.weapons.Chainsaw;
import rs.etf.pmu.cats.db.entities.components.weapons.Rocket;
import rs.etf.pmu.cats.db.entities.components.weapons.Stinger;
import rs.etf.pmu.cats.db.entities.components.wheels.BigWheels;
import rs.etf.pmu.cats.other.RandomGenerator;
import rs.etf.pmu.cats.viewModels.CurrentPlayerViewModel;
import rs.etf.pmu.cats.viewModels.Inventory;
import rs.etf.pmu.cats.viewModels.InventoryViewModel;

public class EntryFragment extends Fragment {

    private CurrentPlayerViewModel currentPlayerViewModel;
    private InventoryViewModel inventoryViewModel;
    private NavController navController;

    private List<Player> allPlayers;
    private Player currentPlayer;
    private int currentIndex;
    private boolean firstTimeLoadingPlayers = true;

    private TextView textViewCurrentPlayer;
    private EditText editTextNickname;
    private Button buttonPlay, buttonRegister;
    private ImageView leftArrow, rightArrow;

    private CatsRoomDatabase catsRoomDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_fragment, container, false);

        currentPlayerViewModel = ViewModelProviders.of(getActivity()).get(CurrentPlayerViewModel.class);
        inventoryViewModel = ViewModelProviders.of(getActivity()).get(InventoryViewModel.class);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        getViews(view);

        catsRoomDatabase = CatsRoomDatabase.getDatabase(getContext());
        loadPlayers();

        configureActions();

        return view;
    }

    private void getViews(View view) {
        textViewCurrentPlayer = view.findViewById(R.id.textViewCurrentPlayer);
        editTextNickname = view.findViewById(R.id.editText_player_nickname);
        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonRegister = view.findViewById(R.id.buttonRegister);
        leftArrow = view.findViewById(R.id.leftArrow);
        rightArrow = view.findViewById(R.id.rightArrow);
    }

    private void loadPlayers() {
        catsRoomDatabase.playerDAO().getAllPlayers().observe(getActivity(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                allPlayers = players;
                if (players.size() != 0) {
                    if (firstTimeLoadingPlayers) currentIndex = 0;
                    else currentIndex = players.size() - 1;
                    currentPlayer = players.get(currentIndex);
                    textViewCurrentPlayer.setText(currentPlayer.getNickname());
                }
            }
        });
    }

    private void configureActions() {

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allPlayers.size() != 0) {
                    currentIndex = (currentIndex + 1) % allPlayers.size();
                    currentPlayer = allPlayers.get(currentIndex);
                    textViewCurrentPlayer.setText(currentPlayer.getNickname());
                }
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allPlayers.size() != 0) {
                    int numOfPlayers = allPlayers.size();
                    currentIndex = (currentIndex + numOfPlayers - 1) % numOfPlayers;
                    currentPlayer = allPlayers.get(currentIndex);
                    textViewCurrentPlayer.setText(currentPlayer.getNickname());
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String nickname = editTextNickname.getText().toString();

                if (nickname != null && nickname.equals("") == false) {
                    try {
                        registerPlayerAndGenerateInventory(view).join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    navController.navigate(R.id.action_entryFragment_to_garageFragment);
                } else {
                    //Player didn't type in nickname
                    Toast.makeText(getContext(), "Enter your nickname!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPlayer != null) {
                    currentPlayerViewModel.setCurrentPlayerLiveData(currentPlayer);
                    try {
                        getInventory().join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    navController.navigate(R.id.action_entryFragment_to_garageFragment);
                } else {
                    Toast.makeText(getContext(), "You haven't picked a player!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        setFullscreenMode();
    }

    private void setFullscreenMode() {
        //hide navigation and status bar
        final View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private Thread registerPlayerAndGenerateInventory(final View view) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final String nickname = editTextNickname.getText().toString();
                Player newPlayer = null;
                try {
                    newPlayer = new Player(nickname);
                    long id = catsRoomDatabase.playerDAO().insert(newPlayer);
                    newPlayer.setId(id);
                    currentPlayerViewModel.postCurrentPlayerLiveData(newPlayer);
                } catch (SQLiteConstraintException e) {
                    //nickname already exists
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "That nickname already exists!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                //Player currentPlayer = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();

                Inventory inventory = new Inventory();

                inventory.currentlyInUse = new ArrayList<>();

                //player is playing for the first time, generate new Components

                List<DrawableComponent> generatedComponents = RandomGenerator.generateForRegistration(newPlayer.id, catsRoomDatabase);
                Chassis chassis = null;
                for (DrawableComponent d : generatedComponents) {
                    if (d instanceof Chassis) {
                        chassis = (Chassis)d;
                        break;
                    }
                }

                generatedComponents.remove(chassis);
                inventory.components = generatedComponents;
                inventory.currentlyInUse.add(chassis);

                inventoryViewModel.postInventoryLiveData(inventory);

                System.out.println("Prvi put");
            }
        });
        t.start();
        return t;
    }

    private Thread getInventory() {
        final Player currentPlayer = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Inventory inventory = new Inventory();

                inventory.components = new ArrayList<>();
                inventory.currentlyInUse = new ArrayList<>();

                List<TankChassis> tankChassisList = catsRoomDatabase.tankChassisDAO().getAllByPlayerIdNow(currentPlayer.getId());
                //check for all chassis

                List<UtilitySlot> currentlyUsedUtilitySlotList = null;
                List<WeaponSlot> currentlyUsedWeaponSlotList = null;
                List<WheelsSlot> currentlyUsedWheelsSlotList = null;
                for (TankChassis t : tankChassisList) {
                    List<UtilitySlot> usl = catsRoomDatabase.utilitySlotDAO().getAllByChassisIdNow(t.id);
                    List<WeaponSlot> wesl = catsRoomDatabase.weaponSlotDAO().getAllByChassisIdNow(t.id);
                    List<WheelsSlot> whsl = catsRoomDatabase.wheelsSlotDAO().getAllByChassisIdNow(t.id);
                    t.slots.addAll(whsl);
                    t.slots.addAll(usl);
                    t.slots.addAll(wesl);

                    if (t.inUse) {
                        inventory.currentlyInUse.add(t);
                        currentlyUsedUtilitySlotList = usl;
                        currentlyUsedWeaponSlotList = wesl;
                        currentlyUsedWheelsSlotList = whsl;
                    } else {
                        inventory.components.add(t);
                    }
                }

                //ovako treba z a svaki Utility, Weapon i Wheels
                List<Stinger> stingerList = catsRoomDatabase.stingerDAO().getAllByPlayerIdNow(currentPlayer.getId());
                for (Stinger s : stingerList) {
                    if (s.slot_id != null) {
                        for (WeaponSlot w : currentlyUsedWeaponSlotList) {
                            if (w.id == s.slot_id) {
                                w.setWeapon(s);
                                inventory.currentlyInUse.add(s);
                                break;
                            }
                        }
                    } else {
                        inventory.components.add(s);
                    }
                }

                List<Chainsaw> chainsawList = catsRoomDatabase.chainsawDAO().getAllByPlayerIdNow(currentPlayer.getId());
                for (Chainsaw s : chainsawList) {
                    if (s.slot_id != null) {
                        for (WeaponSlot w : currentlyUsedWeaponSlotList) {
                            if (w.id == s.slot_id) {
                                w.setWeapon(s);
                                inventory.currentlyInUse.add(s);
                                break;
                            }
                        }
                    } else {
                        inventory.components.add(s);
                    }
                }

                List<Rocket> rocketList = catsRoomDatabase.rocketDAO().getAllByPlayerIdNow(currentPlayer.getId());
                for (Rocket s : rocketList) {
                    if (s.slot_id != null) {
                        for (WeaponSlot w : currentlyUsedWeaponSlotList) {
                            if (w.id == s.slot_id) {
                                w.setWeapon(s);
                                inventory.currentlyInUse.add(s);
                                break;
                            }
                        }
                    } else {
                        inventory.components.add(s);
                    }
                }

                List<Blade> bladeList = catsRoomDatabase.bladeDAO().getAllByPlayerIdNow(currentPlayer.getId());
                for (Blade s : bladeList) {
                    if (s.slot_id != null) {
                        for (WeaponSlot w : currentlyUsedWeaponSlotList) {
                            if (w.id == s.slot_id) {
                                w.setWeapon(s);
                                inventory.currentlyInUse.add(s);
                                break;
                            }
                        }
                    } else {
                        inventory.components.add(s);
                    }
                }


                List<BigWheels> bigWheelsList = catsRoomDatabase.bigWheelsDAO().getAllByPlayerIdNow(currentPlayer.getId());
                for (BigWheels b : bigWheelsList) {
                    if (b.slot_id != null) {
                        for (WheelsSlot w : currentlyUsedWheelsSlotList) {
                            if (w.id == b.slot_id) {
                                w.setWheels(b);
                                inventory.currentlyInUse.add(b);
                                break;
                            }
                        }
                    } else {
                        inventory.components.add(b);
                    }
                }


                System.out.println("Nije prvi put");
                //get other inventory...

                //ovo je samo za testiranje
//                Blade b = new Blade();
//                b.energy = 5;
//                b.health = 200;
//                b.power = 250;
//                b.player_id = 1;
//                b.insert(catsRoomDatabase);
//                inventory.components.add(b);

//                Rocket r = new Rocket();
//                r.energy = 5;
//                r.health = 200;
//                r.power = 250;
//                r.player_id = 1;
//                r.insert(catsRoomDatabase);
//                inventory.components.add(r);
//
//                Stinger s = new Stinger();
//                s.energy = 5;
//                s.health = 200;
//                s.power = 250;
//                s.player_id = 1;
//                s.insert(catsRoomDatabase);
//                inventory.components.add(s);
//
//                BigWheels b = new BigWheels();
//                b.health = 50;
//                b.player_id = 1;
//                b.insert(catsRoomDatabase);
//                inventory.components.add(b);
//
//                TankChassis t = TankChassis.generateRandomTankChassis(1);
//                inventory.components.add(t);
//                t.insert(catsRoomDatabase);
                //do ovde

                inventoryViewModel.postInventoryLiveData(inventory);
            }
        });
        t.start();
        return t;
    }
}
