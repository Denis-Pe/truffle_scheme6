package truffle_scheme6.nodes;

import com.oracle.truffle.api.frame.MaterializedFrame;

public interface MaterializedFrameUser {
    String getRootName();

    MaterializedFrame getMaterializedFrame();

    void setMaterializedFrame(MaterializedFrame materializedFrame);
}
