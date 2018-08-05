package vnext.com.openvidu.activivities;

import android.Manifest;
import android.support.v4.app.DialogFragment;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.MediaStream;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoTrack;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import vnext.com.openvidu.R;
import vnext.com.openvidu.configs.JSONConstants;
import vnext.com.openvidu.fragments.PermissionsDialogFragment;
import vnext.com.openvidu.managers.PeersManager;
import vnext.com.openvidu.models.TokenModel;
import vnext.com.openvidu.network.APIListener;
import vnext.com.openvidu.network.MainApi;
import vnext.com.openvidu.tasks.WebSocketTask;
public class VideoConferenceActivity extends AppCompatActivity implements APIListener{
	private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
	private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101;
	private static final int MY_PERMISSIONS_REQUEST = 102;
	private VideoRenderer remoteRenderer;
	private PeersManager peersManager;
	private WebSocketTask webSocketTask;
	private	MainApi mainApi;
	@BindView(R.id.views_container)
	LinearLayout views_container;
	@BindView(R.id.start_finish_call)
	Button start_finish_call;
	@BindView(R.id.session_name)
	EditText session_name;
	@BindView(R.id.btn_get_token)
	Button btnGetToken;
	@BindView(R.id.participant_name)
	EditText participant_name;
	@BindView(R.id.socketAddress)
	EditText socket_address;
	@BindView(R.id.local_gl_surface_view)
	SurfaceViewRenderer localVideoView;
	@BindView(R.id.main_participant)
	TextView main_participant;
	@BindView(R.id.peer_container)
	FrameLayout peer_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		askForPermissions();
		ButterKnife.bind(this);
		Random random = new Random();
		int randomIndex = random.nextInt(100);
		participant_name.setText(participant_name.getText().append(String.valueOf(randomIndex)));
		this.peersManager = new PeersManager(this, views_container, localVideoView);
		initViews();
		initControls();

	}
	public void initControls(){
		getSession();
	}
	public void getSession(){
		btnGetToken.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mainApi.getSession();
			}
		});
	}
	public LinearLayout getViewsContainer() {
		return views_container;
	}

	public void askForPermissions() {
		if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) &&
				(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
						!= PackageManager.PERMISSION_GRANTED)) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
					MY_PERMISSIONS_REQUEST);
		} else if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.RECORD_AUDIO},
					MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
		} else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.CAMERA},
					MY_PERMISSIONS_REQUEST_CAMERA);
		}
	}

	public void initViews() {
		localVideoView.setMirror(true);
		EglBase rootEglBase = EglBase.create();
		localVideoView.init(rootEglBase.getEglBaseContext(), null);
		localVideoView.setZOrderMediaOverlay(true);
		mainApi= new MainApi(this,this);

	}

	public void start(View view) {
		if (arePermissionGranted()) {
			if (start_finish_call.getText().equals(getResources().getString(R.string.hang_up))) {
				hangup();
				return;
			}
			start_finish_call.setText(getResources().getString(R.string.hang_up));
			socket_address.setEnabled(false);
			socket_address.setFocusable(false);
			session_name.setEnabled(false);
			session_name.setFocusable(false);
			participant_name.setEnabled(false);
			participant_name.setFocusable(false);
			peersManager.start();
			createLocalSocket();
		} else {
			DialogFragment permissionsFragment = new PermissionsDialogFragment();
			permissionsFragment.show(getSupportFragmentManager(),getResources().getString(R.string.permissions_fragment));
		}
	}

	private boolean arePermissionGranted() {
		return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) &&
				(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_DENIED);
	}

	public void createLocalSocket() {
		main_participant.setText(participant_name.getText().toString());
		main_participant.setPadding(20, 3, 20, 3);
		String sessionName = session_name.getText().toString();
		String participantName = participant_name.getText().toString();
//		String socketAddress = socket_address.getText().toString();
		String socketAddress = "https://demo.vnext.work:4443/api/sessions/bzqpnutpbcc4kztt";
		webSocketTask = (WebSocketTask) new WebSocketTask(this, peersManager, sessionName, participantName, socketAddress).execute(this);
	}

	public void gotRemoteStream(MediaStream stream, final RemoteParticipant remoteParticipant) {
		final VideoTrack videoTrack = stream.videoTracks.getFirst();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				remoteRenderer = new VideoRenderer(remoteParticipant.getVideoView());
				remoteParticipant.getVideoView().setVisibility(View.VISIBLE);
				videoTrack.addRenderer(remoteRenderer);
				MediaStream mediaStream = peersManager.getPeerConnectionFactory().createLocalMediaStream(getResources().getString(R.string.label_media_stream_105));
				remoteParticipant.setMediaStream(mediaStream);
				mediaStream.addTrack(peersManager.getLocalAudioTrack());
				mediaStream.addTrack(peersManager.getLocalVideoTrack());
				remoteParticipant.getPeerConnection().removeStream(mediaStream);
				remoteParticipant.getPeerConnection().addStream(mediaStream);
			}
		});
	}

	public void setRemoteParticipantName(String name, RemoteParticipant remoteParticipant) {
		remoteParticipant.getParticipantNameText().setText(name);
		remoteParticipant.getParticipantNameText().setPadding(20, 3, 20, 3);
	}


	public void hangup() {
		try{
		webSocketTask.setCancelled(true);
		peersManager.hangup();
		localVideoView.release();
		start_finish_call.setText(getResources().getString(R.string.start_button));
		socket_address.setEnabled(true);
		socket_address.setFocusableInTouchMode(true);
		session_name.setEnabled(true);
		session_name.setFocusableInTouchMode(true);
		participant_name.setEnabled(true);
		participant_name.setFocusableInTouchMode(true);
		main_participant.setText(null);
		main_participant.setPadding(0, 0, 0, 0);}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		hangup();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		hangup();
		super.onBackPressed();
	}

	@Override
	protected void onStop() {
		hangup();
		super.onStop();
	}
	@Override
	public void onSessionSuccess(String message) {
		try {
			JSONObject data = new JSONObject(message);
			String session = data.getString(JSONConstants.ID);
			TokenModel tokenModel = new TokenModel(session,"SUBSCRIBER","hello");
			mainApi.getTokens(tokenModel);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onSessionError(String message) {
		showMessage(message);
	}
	@Override
	public void onTokenSuccess(String message) {
		showMessage(message);
	}
	@Override
	public void onTokenError(String message) {
		showMessage(message);
	}
	public void showMessage(String message){
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onRequestError(String message) {
		showMessage(message);
	}
}