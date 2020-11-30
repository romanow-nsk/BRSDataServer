package firefighter.dataserver;

import firefighter.core.ServerState;

public interface I_ServerState {
    public void onStateChanged(ServerState serverState);
}
