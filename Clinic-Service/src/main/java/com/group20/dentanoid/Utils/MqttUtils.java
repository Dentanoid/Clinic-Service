package com.group20.dentanoid.Utils;

public class MqttUtils {
    /*
        The arrays below represent all the general types (or keywords) that the codeflow is dependent upon. Each
        keyword forwards the codeflow to the desired method of operation, and these keywords
        are found inside the subscription topics.
    */

    public static String[] topicArtifacts = {
        "clinics",
        "query"
    };

    public static String[] clinicTypes = {
        "dental"
    };

    public static String[] clinicOperations = {
        "register",
        "create",
        "add",
        "remove",
        "delete"
    };

    public static String[] mapTypes = {
        "nearby"
    };

    public static String[] mapOperations = {
        "radius",
        "fixed"
    };


    /*
        The current state of the microservice provides functionality for 2 types of artifacts: Clinics & Queries.
        An extension of a new artifact would entail adding its topics in the string-format below.
    */

    // Clinic topics
    public static String clinicsPublishFormat = "grp20/res/%s/%s/".formatted(clinicTypes[0], topicArtifacts[0]);
    public static String clinicsSubscribeFormat = "grp20/req/%s/%s/".formatted(clinicTypes[0], topicArtifacts[0]);

    // Map topics
    public static String mapPublishFormat = "grp20/res/map/%s".formatted(mapTypes[0]);
    public static String mapSubscribeFormat = "grp20/req/map/%s/%s/".formatted(topicArtifacts[1], mapTypes[0]);

    private static String[] getMapSubscriptions() {
        return new String[] {
            mapSubscribeFormat + "fixed/get",
            mapSubscribeFormat + "radius/get"
        };
    }

    private static String[] getClinicsSubscriptions() {
        return new String[] {
            clinicsSubscribeFormat + "register",
            clinicsSubscribeFormat + "remove",
            clinicsSubscribeFormat + "delete",
            clinicsSubscribeFormat + "add",
            clinicsSubscribeFormat + "get"
        };
    }

    public static String[] getAllSubscriptions() {
        return Utils.concatTwoArrays(getClinicsSubscriptions(), getMapSubscriptions());
    }
}
