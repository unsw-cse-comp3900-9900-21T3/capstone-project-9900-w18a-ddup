import React, { memo } from "react";

function Search({location}) {
    return (
        <div>
            Search {location.state.keyWord}
        </div>
    )
}

export default memo(Search)
