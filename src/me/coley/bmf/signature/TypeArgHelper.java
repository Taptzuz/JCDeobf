package me.coley.bmf.signature;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class TypeArgHelper {
    private Set<String> keys = new LinkedHashSet<>();
    private Map<String, SigArg> typeVars = null;
    private Map<String, List<SigArg>> interfaces = null;

    void addType(String name, SigArg arg) {
        if (typeVars == null) {
            typeVars = new LinkedHashMap<>();
        }
        typeVars.put(name, arg);
        keys.add(name);
    }

    void addInterface(String name, SigArg arg) {
        if (interfaces == null) {
            interfaces = new LinkedHashMap<>();
        }
        if (!interfaces.containsKey(name)) {
            interfaces.put(name, new ArrayList<>());
        }
        interfaces.get(name).add(arg);
        keys.add(name);
    }

    public String toGeneric() {
        StringBuilder strVariables = new StringBuilder();
        if (typeVars == null && interfaces == null) {
            return "";
        }
        strVariables.append("<");
        for (String key : keys) {
            strVariables.append(key + ":");
            if (typeVars != null && typeVars.containsKey(key)) {
                strVariables.append(typeVars.get(key).toArg());
            }
            if (interfaces != null && interfaces.containsKey(key)) {
                strVariables.append(":");
                for (SigArg inter : interfaces.get(key)) {
                    strVariables.append(inter.toArg());
                }
            }
        }
        strVariables.append(">");
        return strVariables.toString();
    }
}