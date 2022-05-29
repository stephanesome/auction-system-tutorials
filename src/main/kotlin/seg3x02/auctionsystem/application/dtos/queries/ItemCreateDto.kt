package seg3x02.auctionsystem.application.dtos.queries

data class ItemCreateDto(val title: String,
                    val description: String) {
    var image: ByteArray? = null
}
