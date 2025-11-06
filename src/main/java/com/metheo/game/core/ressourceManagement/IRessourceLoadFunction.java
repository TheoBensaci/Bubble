package com.metheo.game.core.ressourceManagement;

import java.io.File;
import java.net.URL;

public interface IRessourceLoadFunction<E> {
     E treat(URL data);
}
