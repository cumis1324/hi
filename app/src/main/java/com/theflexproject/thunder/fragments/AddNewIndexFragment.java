package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGDIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGoIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestMapleIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestSimpleProgramIndex;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.IndexLink;


public class AddNewIndexFragment extends BaseFragment {

    private EditText indexLinkView;
    private EditText userNameView;
    private EditText passWordView;
    private Button save;
    private ProgressBar progress_circular;
    private Button save2;
    private ProgressBar progress_circular2;
    Button scanButton;
    ProgressBar loadingScan;
    FrameLayout scanContainer;
    private TextView refreshSuggest;

//    private MaterialButtonToggleGroup toggleIndexTypeGroup;
//    private MaterialButton toggleIndexTypeButton;
//
//    private MaterialButtonToggleGroup toggleFolderTypeGroup;
//    private MaterialButton toggleFolderTypeButton;


    private AutoCompleteTextView actv1;
    private AutoCompleteTextView actv2;

//    private RadioGroup radioIndexTypeGroup;
//    private RadioButton radioIndexTypeButton;
//
//    private RadioGroup radioFolderTypeGroup;
//    private RadioButton radioFolderTypeButton;


    private FirebaseAuth firebaseAuth;


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_index, container, false);
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {

            String url = "https://drive.nfgplusmirror.workers.dev/1:/Movie/";
            String demo = "https://drive.nfgplusmirror.workers.dev/1:/Demo/";
            String series = "https://drive.nfgplusmirror.workers.dev/1:/Series/";
            String typeSeries = "TVShows";
            indexLinkView = view.findViewById(R.id.indexlink);
            userNameView = view.findViewById(R.id.username);
            passWordView = view.findViewById(R.id.password);
            save = view.findViewById(R.id.save);
            progress_circular = view.findViewById(R.id.progress_circular);
            save2 = view.findViewById(R.id.save2);
            progress_circular2 = view.findViewById(R.id.progress_circular2);
            scanButton = mActivity.findViewById(R.id.floating_scan);
            loadingScan = mActivity.findViewById(R.id.loadingScan);
            scanContainer = mActivity.findViewById(R.id.scanContainer);
            refreshSuggest = view.findViewById(R.id.suggestRefresh);

            actv1 = view.findViewById(R.id.actv);
            actv2 = view.findViewById(R.id.actv2);

            String[] index_types = mActivity.getResources().getStringArray(R.array.index_types);
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(mActivity, R.layout.item_index_type, index_types);
            actv1.setAdapter(arrayAdapter1);


            String[] folder_types = mActivity.getResources().getStringArray(R.array.folder_types);
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(mActivity, R.layout.item_folder_type, folder_types);
            actv2.setAdapter(arrayAdapter2);

            String useerid = currentUser.getUid();
            if ("M20Oxpp64gZ480Lqus4afv6x2n63".equals(useerid)){
                save2.setVisibility(View.INVISIBLE);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IndexLink indexLink = new IndexLink();
                    indexLink.setLink(demo);
                    indexLink.setUsername(userNameView.getText().toString());
                    indexLink.setPassword(passWordView.getText().toString());
                    indexLink.setIndexType(actv1.getText().toString());
                    indexLink.setFolderType(actv2.getText().toString());
                    try {
                        if (indexLink.getLink().length() < 1) {
                            indexLinkView.setError("Invalid Link");
                        }
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().find(indexLink.getLink()) != null) {
                                    //refresh instead
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // refreshSuggest.setVisibility(View.VISIBLE);
                                        }
                                    });
                                } else if (indexLink.getLink() != null) {
                                    DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().insert(indexLink);
                                    mActivity.runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            save.setText("Scanning");
                                            progress_circular.setVisibility(View.VISIBLE);
                                            scanButton.setText("Scanning");
                                            loadingScan.setVisibility(View.VISIBLE);
                                        }


                                    });


                                    String folderType = indexLink.getFolderType();
                                    String indexType = indexLink.getIndexType();

                                    String link = indexLink.getLink();
                                    String user = indexLink.getUsername();
                                    String pass = indexLink.getPassword();

                                    System.out.println("Before setting id" + indexLink.getId());

                                    IndexLink indexLinkAgain = DatabaseClient.getInstance(mActivity)
                                            .getAppDatabase()
                                            .indexLinksDao()
                                            .find(link);

                                    int index_id = indexLinkAgain.getId();

                                    System.out.println("After setting id" + indexLinkAgain.getId());

                                    if (folderType.equals("Movies")) {
                                        if (indexType.equals("GDIndex")) {
                                            postRequestGDIndex(link, user, pass, false, index_id);
                                        }
                                        if (indexType.equals("GoIndex")) {
                                            postRequestGoIndex(link, user, pass, false, index_id);
                                        }
                                        if (indexType.equals("Maple")) {
                                            postRequestMapleIndex(link, user, pass, false, index_id);
                                        }
                                        if (indexType.equals("SimpleProgram")) {
                                            postRequestSimpleProgramIndex(link, user, pass, false, index_id);
                                        }
                                    }

                                    if (folderType.equals("TVShows")) {
                                        if (indexType.equals("GDIndex")) {
                                            postRequestGDIndex(link, user, pass, true, index_id);
                                        }
                                        if (indexType.equals("GoIndex")) {
                                            postRequestGoIndex(link, user, pass, true, index_id);
                                        }
                                        if (indexType.equals("MapleIndex")) {
                                            postRequestMapleIndex(link, user, pass, true, index_id);
                                        }
                                        if (indexType.equals("SimpleProgram")) {
                                            postRequestSimpleProgramIndex(link, user, pass, true, index_id);
                                        }
                                    }
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            save.setText("Done");
                                            progress_circular.setVisibility(View.GONE);
                                            scanButton.setText("Done");
                                            loadingScan.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        });
                        thread.start();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if (scanButton.getText().equals("Done")){
                        scanContainer.setVisibility(View.GONE);
                    }
                    if (save.getText().equals("Done")) {
                        mActivity.getSupportFragmentManager().popBackStack();
                    }
                }
            });}
            else {
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IndexLink indexLink = new IndexLink();
                        indexLink.setLink(url);
                        indexLink.setUsername(userNameView.getText().toString());
                        indexLink.setPassword(passWordView.getText().toString());
                        indexLink.setIndexType(actv1.getText().toString());
                        indexLink.setFolderType(actv2.getText().toString());
                        try {
                            if (indexLink.getLink().length() < 1) {
                                indexLinkView.setError("Invalid Link");
                            }
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().find(indexLink.getLink()) != null) {
                                        //refresh instead
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // refreshSuggest.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    } else if (indexLink.getLink() != null) {
                                        DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().insert(indexLink);
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                save2.performClick();
                                                save.setText("Scanning Movies");
                                                progress_circular.setVisibility(View.VISIBLE);
                                                scanButton.setText("Scanning");
                                                loadingScan.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        String folderType = indexLink.getFolderType();
                                        String indexType = indexLink.getIndexType();
                                        String link = indexLink.getLink();
                                        String user = indexLink.getUsername();
                                        String pass = indexLink.getPassword();
                                        System.out.println("Before setting id" + indexLink.getId());
                                        IndexLink indexLinkAgain = DatabaseClient.getInstance(mActivity)
                                                .getAppDatabase()
                                                .indexLinksDao()
                                                .find(link);
                                        int index_id = indexLinkAgain.getId();
                                        System.out.println("After setting id" + indexLinkAgain.getId());
                                        if (folderType.equals("Movies")) {
                                            if (indexType.equals("GDIndex")) {
                                                postRequestGDIndex(link, user, pass, false, index_id);
                                            }
                                            if (indexType.equals("GoIndex")) {
                                                postRequestGoIndex(link, user, pass, false, index_id);
                                            }
                                            if (indexType.equals("Maple")) {
                                                postRequestMapleIndex(link, user, pass, false, index_id);
                                            }
                                            if (indexType.equals("SimpleProgram")) {
                                                postRequestSimpleProgramIndex(link, user, pass, false, index_id);
                                            }
                                        }
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                save.setText("Done");
                                                progress_circular.setVisibility(View.GONE);
                                                scanButton.setText("Done");
                                                loadingScan.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }
                            });
                            thread.start();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (scanButton.getText().equals("Done")){
                            scanContainer.setVisibility(View.GONE);
                        }
                        if (save.getText().equals("All Movies Scanned")) {
                            mActivity.getSupportFragmentManager().popBackStack();
                        }
                    }
                });
                save2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IndexLink indexLink = new IndexLink();
                        indexLink.setLink(series);
                        indexLink.setUsername(userNameView.getText().toString());
                        indexLink.setPassword(passWordView.getText().toString());
                        indexLink.setIndexType(actv1.getText().toString());
                        indexLink.setFolderType(typeSeries);
                        try {
                            if (indexLink.getLink().length() < 1) {
                                indexLinkView.setError("Invalid Link");
                            }
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().find(indexLink.getLink()) != null) {
                                        //refresh instead
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // refreshSuggest.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    } else if (indexLink.getLink() != null) {
                                        DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().insert(indexLink);
                                        mActivity.runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                RelativeLayout save2container;
                                                save2container = view.findViewById(R.id.save_container2);
                                                save2container.setVisibility(View.VISIBLE);
                                                save2.setText("Scanning Series");

                                                progress_circular2.setVisibility(View.VISIBLE);
                                            }


                                        });


                                        String folderType = indexLink.getFolderType();
                                        String indexType = indexLink.getIndexType();

                                        String link = indexLink.getLink();
                                        String user = indexLink.getUsername();
                                        String pass = indexLink.getPassword();

                                        System.out.println("Before setting id" + indexLink.getId());

                                        IndexLink indexLinkAgain = DatabaseClient.getInstance(mActivity)
                                                .getAppDatabase()
                                                .indexLinksDao()
                                                .find(link);

                                        int index_id = indexLinkAgain.getId();

                                        System.out.println("After setting id" + indexLinkAgain.getId());

                                        if (folderType.equals("TVShows")) {
                                            if (indexType.equals("GDIndex")) {
                                                postRequestGDIndex(link, user, pass, true, index_id);
                                            }
                                            if (indexType.equals("GoIndex")) {
                                                postRequestGoIndex(link, user, pass, true, index_id);
                                            }
                                            if (indexType.equals("MapleIndex")) {
                                                postRequestMapleIndex(link, user, pass, true, index_id);
                                            }
                                            if (indexType.equals("SimpleProgram")) {
                                                postRequestSimpleProgramIndex(link, user, pass, true, index_id);
                                            }
                                        }
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                save2.setText("All Series Scanned");
                                                progress_circular2.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }
                            });
                            thread.start();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (save.getText().equals("All Movies Scanned")) {
                            mActivity.getSupportFragmentManager().popBackStack();
                        }
                    }
                });
            }
        }

    }
}