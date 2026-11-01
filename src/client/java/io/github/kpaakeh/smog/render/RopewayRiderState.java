package io.github.kpaakeh.smog.render;

/** Per-entity render state, shared by player skin and humanoid equipment models. */
public interface RopewayRiderState {
    boolean smog$isRopewayRider();
    void smog$setRopewayRider(boolean riding);
}
