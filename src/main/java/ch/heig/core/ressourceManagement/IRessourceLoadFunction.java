/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Interface use to make lambda for resource loading
 */

package ch.heig.core.ressourceManagement;

import java.net.URL;

public interface IRessourceLoadFunction<E> {
     E treat(URL data);
}
