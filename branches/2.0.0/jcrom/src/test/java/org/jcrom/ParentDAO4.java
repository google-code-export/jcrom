/**
 * Copyright (C) Olafur Gauti Gudmundsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jcrom;

import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import org.jcrom.dao.AbstractJcrDAO;

/**
 *
 * @author Olafur Gauti Gudmundsson
 */
public class ParentDAO4 extends AbstractJcrDAO<Parent4> {

    private static final String[] MIXIN_TYPES = { NodeType.MIX_REFERENCEABLE };

    public ParentDAO4(Session session, Jcrom jcrom) {
        super(Parent4.class, session, jcrom, MIXIN_TYPES);
    }

}