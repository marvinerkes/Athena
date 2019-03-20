/*
 * Copyright (c) 2017 "Marvin Erkes"
 *
 * This file is part of Athena.
 *
 * Athena is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.progme.athena.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Marvin Erkes on 11.10.2015.
 */
public class AthenaDispatcher {

    /**
     * The executor service for the async operations.
     */
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Dispatches DispatcherConsumer.execute async.
     *
     * @param consumer the callback.
     */
    public void dispatch(DispatcherConsumer consumer) {

        executorService.execute(consumer::execute);
    }

    /**
     * Submits a DispatcherConsumer callback async an returns a future to wait for completion if you want.
     *
     * @param consumer the callback.
     * @return the future.
     */
    public Future<?> submit(DispatcherConsumer consumer) {

        return executorService.submit(consumer::execute);
    }

    /**
     * Closes the executor service.
     */
    public void close() {

        executorService.shutdown();
    }
}
