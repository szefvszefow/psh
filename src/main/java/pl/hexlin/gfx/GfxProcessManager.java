package pl.hexlin.gfx;

import pl.hexlin.servermember.ServerMember;
import pl.hexlin.voting.Election;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GfxProcessManager
{
    public List<GfxProcess> gfxProcessesList = new ArrayList<>();
    public void addGfx(GfxProcess gfxProcess) {
        gfxProcessesList.add(gfxProcess);
    }

    public void removeGfx(GfxProcess gfxProcess) {
        gfxProcessesList.remove(gfxProcess);
    }

    public GfxProcess getGfxProcess(String userId) {
        return gfxProcessesList.stream()
                .filter(gfxProcess -> gfxProcess.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}