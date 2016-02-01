package com.yahoo.reportr.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.reportr.data.entity.Ontology;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by bhavanis on 1/8/16.
 */
public class OntologyUtil {

    public List<Ontology> getOntologyList(String result) {
        JsonArray results = new JsonParser().parse(result).getAsJsonObject().getAsJsonArray("kids");
        List<Ontology> list = new ArrayList<>();
        for (JsonElement kid : results) {
            Ontology o = new Ontology();
            JsonObject child = kid.getAsJsonObject();
            o.setId(child.get("id").getAsInt());
            o.setName(child.get("name").getAsString());
            o.setSortOrder(child.get("sortOrder").getAsInt());
            SortedSet<Ontology> set = getChildren(child.getAsJsonArray("kids"));
            o.setKids(set);
            list.add(o);
        }
        return list;
    }

    public SortedSet<Ontology> getChildren(JsonArray results) {
        SortedSet<Ontology> children = new TreeSet<Ontology>();
        for (JsonElement kid : results) {
            Ontology o = new Ontology();
            JsonObject child = kid.getAsJsonObject();
            o.setId(child.get("id").getAsInt());
            o.setName(child.get("name").getAsString());
            o.setSortOrder(child.get("sortOrder").getAsInt());
            o.setKids(getChildren(child.getAsJsonArray("kids")));
            children.add(o);
        }
        return children;
    }
}
