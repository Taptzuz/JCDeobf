package me.coley.bmf.mapping;

import java.util.ArrayList;
import java.util.List;

import me.coley.bmf.util.Box;

public class ClassMapping extends AbstractMapping {
    private final List<MemberMapping> members = new ArrayList<>();

    public ClassMapping(String name) {
        super(name);
    }

    public ClassMapping(Box<String> name) {
        super(name);
    }

    /**
     * Gets a member's mapping by the original name and descriptor.
     * 
     * @param name
     * @param desc
     * @return
     */
    public MemberMapping getMemberMapping(String name, String desc) {
        for (MemberMapping mm : members) {
            if (mm.name.original.equals(name) && mm.desc.original.equals(desc)) {
                return mm;
            }
        }
        return null;
    }

    /**
     * Gets a member's mapping by the current name and descriptor.
     * 
     * @param name
     * @param desc
     * @return
     */
    public MemberMapping getMemberMappingWithRenaming(String name, String desc) {
        for (MemberMapping mm : members) {
            if (mm.name.getValue().equals(name) && mm.desc.toDesc().equals(desc))
                return mm;
        }
        return null;
    }

    /**
     * Gets the list of members in the class.
     * 
     * @return
     */
    public List<MemberMapping> getMembers() {
        return members;
    }

    /**
     * Adds a member to the class. The passed member is replaced with another
     * Member instance if the name and descriptor match the stored version.
     * 
     * @param mapping
     * @param mm
     */
    public void addMember(Mapping mapping, MemberMapping mm) {
        if (mm instanceof MethodMapping) {
            mm = mapping.getMemberInstance(this, mm);
        }
        members.add(mm);
    }

    /**
     * Returns a list of members matching the given name. Compares to a member's
     * original name rather than current name.
     * 
     * @param name
     * @return
     */
    public List<MemberMapping> getMembersByOriginalName(String name) {
        List<MemberMapping> list = new ArrayList<MemberMapping>();
        for (MemberMapping mm : members) {
            if (mm.name.original.equals(name))
                list.add(mm);
        }
        return list;
    }

    /**
     * Returns a list of members matching the given name.
     * 
     * @param name
     * @return
     */
    public List<MemberMapping> getMembersByName(String name) {
        List<MemberMapping> list = new ArrayList<MemberMapping>();
        for (MemberMapping mm : members) {
            if (mm.name.getValue().equals(name))
                list.add(mm);
        }
        return list;
    }

}
