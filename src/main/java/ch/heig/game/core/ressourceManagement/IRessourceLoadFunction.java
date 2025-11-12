package ch.heig.game.core.ressourceManagement;

import java.net.URL;

public interface IRessourceLoadFunction<E> {
     E treat(URL data);
}
