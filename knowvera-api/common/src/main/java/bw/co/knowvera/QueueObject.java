package bw.co.knowvera;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;


//QueueObject
public record QueueObject (
    // Record attributes
    /**
     * TODO: Model Documentation for bw.co.knowvera.QueueObject.objectId
     */
    java.lang.String objectId,    /**
     * TODO: Model Documentation for bw.co.knowvera.QueueObject.target
     */
    bw.co.knowvera.TargetEntity target,    /**
     * TODO: Model Documentation for bw.co.knowvera.QueueObject.targetId
     */
    java.lang.String targetId
) implements Serializable {
}
