package bw.co.knowvera.processing.profile;

import java.util.List;

/**
 * The latest version of a profile, for pickers (e.g. when linking a document type to a profile).
 */
public record ProfileSummary(String code, int version, String name, String description, List<StepType> steps) {
}
