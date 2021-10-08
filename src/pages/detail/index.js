import React, { memo } from "react";

function Detail({match}) {
    return (
        <div>
            Detail {match.params.id}
        </div>
    )
}

export default memo(Detail)
