package com.example.musuemguide.application

import android.app.Application
import com.example.musuemguide.R
import com.example.musuemguide.localStorage.LocalRepository
import com.example.musuemguide.localStorage.local.ArtifactModel
import com.example.musuemguide.localStorage.local.HeaderModel
import com.example.musuemguide.localStorage.local.LocalDatabase
import com.example.musuemguide.localStorage.local.asArtifactDTO
import com.example.musuemguide.localStorage.local.asArtifactModel
import com.example.musuemguide.userSection.viewmodels.DetailsViewModel
import com.example.musuemguide.userSection.viewmodels.HomeViewModel
import com.example.musuemguide.userSection.viewmodels.RobotControlViewModel
import com.example.musuemguide.userSection.viewmodels.SettingsViewModel
import com.example.musuemguide.utils.BitmapConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MuseumGuideApp : Application() {


    private val database by lazy { LocalDatabase.getDatabase(this) }
    val repository by lazy { LocalRepository(database.artifactDao()) }

    override fun onCreate() {
        super.onCreate()
        /**
         * use Koin Library as a service locator
         */
        val myModule = module {

            single { LocalDatabase.getDatabase(androidContext()) }
            single { LocalRepository(LocalDatabase.getDatabase(this@MuseumGuideApp).artifactDao()) }


            single {
                SettingsViewModel(
                    get()
                )
            }

            single {
                RobotControlViewModel(
                    get()
                )
            }

            single {
                HomeViewModel(
                    get()
                )
            }

            single {
                DetailsViewModel(
                    get()
                )
            }
        }

        startKoin {
            androidContext(this@MuseumGuideApp)
            modules(listOf(myModule))
        }


        val artifacts: List<ArtifactModel> = listOf(
            HeaderModel(
                title = "Statues",
                type = "HEADER",
            ).asArtifactModel(),


            ArtifactModel(
                title = getString(R.string.nefertiti),
                details = getString(R.string.akhenaten_history),
                image = BitmapConverter.bitmapToString(R.drawable.nefertiti_img, this),
                category = "Status",
                type = "BODY"
            ),

            ArtifactModel(
                title = getString(R.string.amenhotep_i),
                details = getString(R.string.hatshepsut_history_text),
                image = BitmapConverter.bitmapToString(R.drawable.hatshepsut_img, this),
                category = "Status",
                type = "BODY"
            ),

            HeaderModel(
                title = "Potteries",
                type = "HEADER",
            ).asArtifactModel(),

            ArtifactModel(
                title = getString(R.string.akhenaten),
                details = getString(R.string.thuthmose_iii_history),
                image = BitmapConverter.bitmapToString(R.drawable.tutankhamun_img, this),
                category = "Potteries",
                type = "BODY"
            ),

            ArtifactModel(
                title = getString(R.string.thuthmose_iii),
                details = getString(R.string.thuthmose_i_history),
                image = BitmapConverter.bitmapToString(R.drawable.narmer_img, this),
                category = "Potteries",
                type = "BODY"
            )

        )
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAllArtifacts()
            repository.addArtifacts(artifacts.asArtifactDTO())

        }
    }
}