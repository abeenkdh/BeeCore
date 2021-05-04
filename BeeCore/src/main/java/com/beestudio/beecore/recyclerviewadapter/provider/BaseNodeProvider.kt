package com.beestudio.beecore.recyclerviewadapter.provider

import com.beestudio.beecore.recyclerviewadapter.BaseNodeAdapter
import com.beestudio.beecore.recyclerviewadapter.entity.node.BaseNode

abstract class BaseNodeProvider : BaseItemProvider<BaseNode>() {

    override fun getAdapter(): BaseNodeAdapter? {
        return super.getAdapter() as? BaseNodeAdapter
    }

}